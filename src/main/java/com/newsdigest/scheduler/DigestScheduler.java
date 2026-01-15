package com.newsdigest.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DigestScheduler {

    @Scheduled(fixedRate = 300000) // 5 minutos
    public void processDigests() {
        // 1. buscar usuários ativos
        // 2. filtrar por horário
        // 3. delegar geração de digest
    }
}
