package com.newsdigest.controller;

import com.newsdigest.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<Void>> inscrever(
            @PathVariable Long userId,
            @RequestBody CreateUserSubscriptionRequest request) {

        try {
            service.inscrever(
                    userId,
                    request.getNewsSourceId(),
                    request.getQuantidadeNoticias()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            true,
                            "Inscrição realizada com sucesso",
                            null
                    ));

        } catch (IllegalArgumentException e) {

            log.warn("Erro ao inscrever usuário: {}", e.getMessage());

            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    ));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserSubscriptionResponse>>> listar(
            @PathVariable Long userId) {

        List<UserSubscriptionResponse> response = service.listar(userId).stream()
                .map(UserSubscriptionResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Lista de inscrições",
                        response
                )
        );
    }

    @DeleteMapping("/{newsSourceId}")
    public ResponseEntity<ApiResponse<Void>> remover(
            @PathVariable Long userId,
            @PathVariable Long newsSourceId) {

        try {
            service.remover(userId, newsSourceId);

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Inscrição removida com sucesso",
                            null
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    ));
        }
    }
}
