package com.newsdigest.controller;

import com.newsdigest.domain.User;
import com.newsdigest.dto.CreateUserRequest;
import com.newsdigest.dto.UpdateUserHorarioRequest;
import com.newsdigest.dto.UserResponse;
import com.newsdigest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> buscarUsuarioPorId(@PathVariable Long id) {
        return userService.buscarUsuarioPorId(id)
                .map(user -> ResponseEntity.ok(UserResponse.fromEntity(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET todos os usuários ativos
    @GetMapping
    public ResponseEntity<List<UserResponse>> listarUsuarios() {
        List<UserResponse> usuarios = userService.listarUsuarios().stream()
                .map(UserResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<UserResponse> cadastrarUsuario(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.cadastrarUsuario(request.getEmail(), request.getHorarioEnvio());
            return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromEntity(user));
        } catch (IllegalArgumentException e) {
            log.warn("Erro ao cadastrar usuário: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/horario")
    public ResponseEntity<Void> alterarHorarioEnvio(@PathVariable Long id, @RequestBody UpdateUserHorarioRequest request) {
        try {
            userService.alterarHorarioEnvio(id, request.getHorarioEnvio());
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarUsuario(@PathVariable Long id) {
        try {
            userService.desativarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

