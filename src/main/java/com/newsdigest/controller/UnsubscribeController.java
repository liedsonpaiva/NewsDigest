package com.newsdigest.controller;

import com.newsdigest.domain.User;
import com.newsdigest.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/unsubscribe")
public class UnsubscribeController {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UnsubscribeController.class);

    public UnsubscribeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<String> unsubscribe(@RequestParam("token") String token) {
        return userRepository.findByTokenCancelamento(token)
                .map(user -> {
                    user.setAtivo(false);
                    userRepository.save(user);
                    return ResponseEntity.ok("Você foi descadastrado com sucesso do NewsDigest.");
                })
                .orElseGet(() -> ResponseEntity.badRequest().body("Token inválido ou usuário não encontrado."));
    }
}
