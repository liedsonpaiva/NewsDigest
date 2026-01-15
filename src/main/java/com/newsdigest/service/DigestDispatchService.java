package com.newsdigest.service;

import com.newsdigest.domain.User;
import com.newsdigest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DigestDispatchService {

    private final UserRepository userRepository;

    public DigestDispatchService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void processPendingDigests() {
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();

        // 1. usuários ativos cujo horário já passou
        List<User> candidates = userRepository.findByAtivoTrueAndHorarioEnvioLessThanEqual(now);

        // 2. filtra quem ainda NÃO recebeu hoje
        List<User> eligibleUsers = candidates.stream().filter(u -> u.getDataUltimoEnvioDigest() == null || !u.getDataUltimoEnvioDigest().isEqual(today)).collect(Collectors.toList());

        for (User user : eligibleUsers) {
            // Fase 5: só marca como enviado
            // Fase 6: aqui entra geração + envio de email
            user.setDataUltimoEnvioDigest(today);
            userRepository.save(user);
        }
    }
}
