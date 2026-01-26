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

    private final UserRepository userRepository;
    private final DigestEligibilityService eligibilityService;
    private final UserDigestProcessor processor;

    public DigestDispatchService(
            UserRepository userRepository,
            DigestEligibilityService eligibilityService,
            UserDigestProcessor processor) {
        this.userRepository = userRepository;
        this.eligibilityService = eligibilityService;
        this.processor = processor;
    }

    @Transactional
    public void processPendingDigests() {

        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();

        List<User> users = userRepository.findAll();

        users.stream()
                .filter(user -> eligibilityService.isEligible(user, now, today))
                .forEach(user -> processor.process(user, today));
    }
}
