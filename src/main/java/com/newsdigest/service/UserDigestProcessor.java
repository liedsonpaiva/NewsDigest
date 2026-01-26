package com.newsdigest.service;

import com.newsdigest.domain.NewsSource;
import com.newsdigest.domain.User;
import com.newsdigest.domain.UserSubscription;
import com.newsdigest.repository.UserSubscriptionRepository;
import com.newsdigest.rss.RssOrchestrator;
import com.newsdigest.rss.dto.RssItem;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserDigestProcessor {

    private final UserSubscriptionRepository subscriptionRepository;
    private final RssOrchestrator rssOrchestrator;
    private final DigestTemplateBuilder templateBuilder;
    private final DigestSendService sendService;

    public UserDigestProcessor(
            UserSubscriptionRepository subscriptionRepository,
            RssOrchestrator rssOrchestrator,
            DigestTemplateBuilder templateBuilder,
            DigestSendService sendService) {
        this.subscriptionRepository = subscriptionRepository;
        this.rssOrchestrator = rssOrchestrator;
        this.templateBuilder = templateBuilder;
        this.sendService = sendService;
    }

    public void process(User user, LocalDate today) {

        List<UserSubscription> subscriptions = subscriptionRepository.findByUser(user);

        if (subscriptions.isEmpty()) return;

        Map<NewsSource, Integer> sourcesWithLimits = subscriptions.stream().collect(Collectors.toMap(UserSubscription::getSource, UserSubscription::getQuantidadeNoticias));

        Map<String, List<RssItem>> feeds = fetchFeeds(sourcesWithLimits);

        String html = templateBuilder.build(user, feeds);

        sendService.send(user, html, today);
    }

    private Map<String, List<RssItem>> fetchFeeds(
            Map<NewsSource, Integer> sourcesWithLimits) {

        Map<String, List<RssItem>> result = new HashMap<>();

        for (Map.Entry<NewsSource, Integer> entry : sourcesWithLimits.entrySet()) {

            NewsSource source = entry.getKey();
            int limit = entry.getValue();

            try {
                List<RssItem> items = rssOrchestrator.fetchAll(List.of(source), limit).getOrDefault(source, List.of());

                result.put(source.getNome(), items);

            } catch (Exception e) {
                result.put(source.getNome(), List.of());
            }
        }

        return result;
    }
}

