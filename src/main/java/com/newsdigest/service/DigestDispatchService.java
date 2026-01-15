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

    public DigestDispatchService(UserRepository userRepository, UserSubscriptionRepository subscriptionRepository, RssOrchestrator rssOrchestrator, DigestTemplateBuilder templateBuilder, EmailService emailService) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.rssOrchestrator = rssOrchestrator;
        this.templateBuilder = templateBuilder;
        this.emailService = emailService;
    }

    public void processPendingDigests() {
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();

        List<User> candidates = userRepository.findByAtivoTrueAndHorarioEnvioLessThanEqual(now);

        List<User> eligibleUsers = candidates.stream().filter(u -> u.getDataUltimoEnvioDigest() == null || !u.getDataUltimoEnvioDigest().isEqual(today)).toList();

        for (User user : eligibleUsers) {

            List<UserSubscription> subscriptions = subscriptionRepository.findByUser(user);

            if (subscriptions.isEmpty()) {continue;}

            List<NewsSource> sources = subscriptions.stream().map(UserSubscription::getSource).toList();

            Map<NewsSource, List<RssItem>> feeds =
                    rssOrchestrator.fetchAll(sources, 5);

            Map<String, List<RssItem>> feedsByName = feeds.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getNome(), Map.Entry::getValue));

            String html = templateBuilder.build(user, feedsByName);

            emailService.sendHtml(user.getEmail(), "Your News Digest", html);

            user.setDataUltimoEnvioDigest(today);
            userRepository.save(user);
        }
    }
}
