package com.newsdigest.controller;

import com.newsdigest.domain.UserSubscription;
import com.newsdigest.dto.CreateUserSubscriptionRequest;
import com.newsdigest.dto.UserSubscriptionResponse;
import com.newsdigest.service.UserSubscriptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/subscriptions")
public class UserSubscriptionController {

    private final UserSubscriptionService service;

    public UserSubscriptionController(UserSubscriptionService service) {
        this.service = service;
    }

    @PostMapping
    public void inscrever(@PathVariable Long userId, @RequestBody CreateUserSubscriptionRequest request) {
        service.inscrever(userId, request.getNewsSourceId(), request.getQuantidadeNoticias());
    }

    @GetMapping
    public List<UserSubscriptionResponse> listar(@PathVariable Long userId) {
        return service.listar(userId).stream().map(UserSubscriptionResponse::fromEntity).toList();
    }

    @DeleteMapping("/{newsSourceId}")
    public void remover(@PathVariable Long userId, @PathVariable Long newsSourceId) {
        service.remover(userId, newsSourceId);
    }
}