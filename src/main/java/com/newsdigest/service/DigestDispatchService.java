package com.newsdigest.service;

import com.newsdigest.domain.NewsSource;
import com.newsdigest.domain.User;
import com.newsdigest.domain.UserSubscription;
import com.newsdigest.email.EmailService;
import com.newsdigest.repository.UserRepository;
import com.newsdigest.repository.UserSubscriptionRepository;
import com.newsdigest.rss.RssOrchestrator;
import com.newsdigest.rss.dto.RssItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DigestDispatchService {

    private static final Logger log = LoggerFactory.getLogger(DigestDispatchService.class);

    private final UserRepository userRepository;
    private final UserSubscriptionRepository subscriptionRepository;
    private final RssOrchestrator rssOrchestrator;
    private final DigestTemplateBuilder templateBuilder;
    private final EmailService emailService;

    public DigestDispatchService(UserRepository userRepository,
                                 UserSubscriptionRepository subscriptionRepository,
                                 RssOrchestrator rssOrchestrator,
                                 DigestTemplateBuilder templateBuilder,
                                 EmailService emailService) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.rssOrchestrator = rssOrchestrator;
        this.templateBuilder = templateBuilder;
        this.emailService = emailService;
    }

    @Transactional
    public void processPendingDigests() {
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();

        log.info("Scheduler executando em: {}", now);

        List<User> candidates =
                userRepository.findByAtivoTrueAndHorarioEnvioLessThanEqual(now);

        log.info("Usuários candidatos encontrados: {}", candidates.size());

        List<User> eligibleUsers = candidates.stream()
                .filter(u -> u.getDataUltimoEnvioDigest() == null
                        || !u.getDataUltimoEnvioDigest().isEqual(today))
                .toList();

        log.info("Usuários elegíveis para envio hoje: {}", eligibleUsers.size());

        for (User user : eligibleUsers) {
            log.info("Processando usuário: {}", user.getEmail());

            try {
                List<UserSubscription> subscriptions =
                        subscriptionRepository.findByUser(user);

                if (subscriptions.isEmpty()) {
                    log.info("Usuário {} não tem assinaturas. Pulando.", user.getEmail());
                    continue;
                }

                List<NewsSource> sources = subscriptions.stream()
                        .map(UserSubscription::getSource)
                        .toList();

                Map<NewsSource, List<RssItem>> feeds;
                try {
                    feeds = rssOrchestrator.fetchAll(sources, 5);
                } catch (Exception e) {
                    log.error("Erro ao buscar RSS para usuário {}", user.getEmail(), e);
                    feeds = Map.of();
                }

                Map<String, List<RssItem>> feedsByName = feeds.entrySet().stream()
                        .collect(Collectors.toMap(
                                e -> e.getKey().getNome(),
                                Map.Entry::getValue
                        ));

                String html = templateBuilder.build(user, feedsByName);

                try {
                    emailService.sendHtml(
                            user.getEmail(),
                            "Your News Digest",
                            html
                    );
                    user.setDataUltimoEnvioDigest(today);
                    userRepository.save(user);

                    log.info(
                            "E-mail enviado e dataUltimoEnvioDigest atualizada para {} (usuário {})",
                            today,
                            user.getEmail()
                    );
                } catch (Exception e) {
                    log.error("Erro ao enviar e-mail para {}", user.getEmail(), e);
                }

            } catch (Exception e) {
                log.error("Erro inesperado ao processar usuário {}", user.getEmail(), e);
            }
        }
    }
}
