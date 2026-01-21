package com.newsdigest.service;

import com.newsdigest.domain.NewsSource;
import com.newsdigest.domain.User;
import com.newsdigest.domain.UserSubscription;
import com.newsdigest.email.EmailService;
import com.newsdigest.repository.UserRepository;
import com.newsdigest.repository.UserSubscriptionRepository;
import com.newsdigest.rss.RssOrchestrator;
import com.newsdigest.rss.dto.RssItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DigestDispatchService {

    private final UserRepository userRepository;
    private final UserSubscriptionRepository subscriptionRepository;
    private final RssOrchestrator rssOrchestrator;
    private final DigestTemplateBuilder templateBuilder;
    private final EmailService emailService;

    public DigestDispatchService(UserRepository userRepository, UserSubscriptionRepository subscriptionRepository,
                                 RssOrchestrator rssOrchestrator, DigestTemplateBuilder templateBuilder,
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

        System.out.println("Scheduler executando em: " + now);

        List<User> candidates = userRepository.findByAtivoTrueAndHorarioEnvioLessThanEqual(now);

        System.out.println("Usuários candidatos encontrados: " + candidates.size());

        List<User> eligibleUsers = candidates.stream()
                .filter(u -> u.getDataUltimoEnvioDigest() == null || !u.getDataUltimoEnvioDigest().isEqual(today))
                .toList();

        System.out.println("Usuários elegíveis para envio hoje: " + eligibleUsers.size());

        for (User user : eligibleUsers) {
            System.out.println("Processando usuário: " + user.getEmail());

            try {
                List<UserSubscription> subscriptions = subscriptionRepository.findByUser(user);

                if (subscriptions.isEmpty()) {
                    System.out.println("Usuário " + user.getEmail() + " não tem assinaturas. Pulando.");
                    continue;
                }

                List<NewsSource> sources = subscriptions.stream()
                        .map(UserSubscription::getSource)
                        .toList();

                // Tenta buscar RSS, mas falha não derruba o job
                Map<NewsSource, List<RssItem>> feeds;
                try {
                    feeds = rssOrchestrator.fetchAll(sources, 5);
                } catch (Exception e) {
                    System.err.println("Erro ao buscar RSS para usuário " + user.getEmail() + ": " + e.getMessage());
                    feeds = Map.of(); // fallback vazio
                }

                Map<String, List<RssItem>> feedsByName = feeds.entrySet().stream()
                        .collect(Collectors.toMap(e -> e.getKey().getNome(), Map.Entry::getValue));

                String html = templateBuilder.build(user, feedsByName);

                // Tenta enviar e-mail, mas falha não derruba o job
                try {
                    emailService.sendHtml(user.getEmail(), "Your News Digest", html);
                    user.setDataUltimoEnvioDigest(today);
                    userRepository.save(user);
                    System.out.println("E-mail enviado e dataUltimoEnvioDigest atualizada para: " + today);
                } catch (Exception e) {
                    System.err.println("Erro ao enviar e-mail para " + user.getEmail() + ": " + e.getMessage());
                }
            } catch (Exception e) {
                // Captura qualquer outro erro inesperado e continua para o próximo usuário
                System.err.println("Erro inesperado ao processar usuário " + user.getEmail() + ": " + e.getMessage());
            }
        }
    }
}
