package com.newsdigest.controller;

import com.newsdigest.domain.User;
import com.newsdigest.dto.ApiResponse;
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> buscarUsuarioPorId(@PathVariable Long id) {
        return userService.buscarUsuarioPorId(id)
                .map(user -> ResponseEntity.ok(
                        new ApiResponse<>(true, "Usuário encontrado", UserResponse.fromEntity(user))
                ))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ApiResponse<>(false, "Usuário não encontrado", null)
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> listarUsuarios() {
        List<UserResponse> usuarios = userService.listarUsuarios().stream()
                .map(UserResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Lista de usuários", usuarios)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> cadastrarUsuario(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.cadastrarUsuario(request.getEmail(), request.getHorarioEnvio());

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponse<>(true, "Usuário cadastrado com sucesso", UserResponse.fromEntity(user))
            );

        } catch (IllegalArgumentException e) {
            log.warn("Erro ao cadastrar usuário: {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PutMapping("/{id}/horario")
    public ResponseEntity<ApiResponse<Void>> alterarHorarioEnvio(
            @PathVariable Long id,
            @RequestBody UpdateUserHorarioRequest request) {

        try {
            userService.alterarHorarioEnvio(id, request.getHorarioEnvio());

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Horário atualizado com sucesso", null)
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> desativarUsuario(@PathVariable Long id) {

        try {
            userService.desativarUsuario(id);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Usuário desativado com sucesso", null)
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }
}
