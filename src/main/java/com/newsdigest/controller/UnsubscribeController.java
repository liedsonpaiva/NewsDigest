package com.newsdigest.controller;

import com.newsdigest.domain.User;
import com.newsdigest.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/unsubscribe")
public class UnsubscribeController {

    private final UserRepository userRepository;

    public UnsubscribeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<String> unsubscribe(@RequestParam("token") String token) {
        Optional<User> userOpt = userRepository.findByTokenCancelamento(token);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Token inválido ou usuário não encontrado.");
        }

        User user = userOpt.get();
        user.setAtivo(false); // desativa usuário
        userRepository.save(user);

        return ResponseEntity.ok("Você foi descadastrado com sucesso do NewsDigest.");
    }
}
