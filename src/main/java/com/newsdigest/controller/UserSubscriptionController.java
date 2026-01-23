package com.newsdigest.controller;

import com.newsdigest.domain.UserSubscription;
import com.newsdigest.dto.CreateUserSubscriptionRequest;
import com.newsdigest.dto.UserSubscriptionResponse;
import com.newsdigest.service.UserSubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users/{userId}/subscriptions")
public class UserSubscriptionController {

    private final UserSubscriptionService service;
    private static final Logger log = LoggerFactory.getLogger(UserSubscriptionController.class);

    public UserSubscriptionController(UserSubscriptionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> inscrever(@PathVariable Long userId, @RequestBody CreateUserSubscriptionRequest request) {
        try {
            service.inscrever(userId, request.getNewsSourceId(), request.getQuantidadeNoticias());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UserSubscriptionResponse>> listar(@PathVariable Long userId) {
        List<UserSubscriptionResponse> response = service.listar(userId).stream()
                .map(UserSubscriptionResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{newsSourceId}")
    public ResponseEntity<Void> remover(@PathVariable Long userId, @PathVariable Long newsSourceId) {
        service.remover(userId, newsSourceId);
        return ResponseEntity.noContent().build();
    }
}
