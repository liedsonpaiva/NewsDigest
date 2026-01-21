package com.newsdigest.scheduler;

import com.newsdigest.service.DigestDispatchService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DigestScheduler {

    private final DigestDispatchService dispatchService;

    public DigestScheduler(DigestDispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }

    @Scheduled(fixedRate = 60000) // 1 minuto
    public void processDigests() {
        dispatchService.processPendingDigests();
    }
}
