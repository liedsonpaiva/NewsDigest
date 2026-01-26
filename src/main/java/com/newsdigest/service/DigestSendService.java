package com.newsdigest.service;

import com.newsdigest.domain.User;
import com.newsdigest.email.EmailService;
import com.newsdigest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DigestSendService {

    private final EmailService emailService;
    private final UserRepository userRepository;

    public DigestSendService(
            EmailService emailService,
            UserRepository userRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    public void send(User user, String html, LocalDate today) {

        emailService.sendHtml(user.getEmail(), "Your News Digest", html);

        user.setDataUltimoEnvioDigest(today);
        userRepository.save(user);
    }
}

