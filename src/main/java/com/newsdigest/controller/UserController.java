package com.newsdigest.controller;

import com.newsdigest.domain.User;
import com.newsdigest.dto.CreateUserRequest;
import com.newsdigest.dto.UpdateUserHorarioRequest;
import com.newsdigest.dto.UserResponse;
import com.newsdigest.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse cadastrarUsuario(@RequestBody CreateUserRequest request) {
        User user = userService.cadastrarUsuario(request.getEmail(), request.getHorarioEnvio());

        return UserResponse.fromEntity(user);
    }

    @PutMapping("/{id}/horario")
    public void alterarHorarioEnvio(@PathVariable Long id, @RequestBody UpdateUserHorarioRequest request) {
        userService.alterarHorarioEnvio(id, request.getHorarioEnvio());
    }

    @DeleteMapping("/{id}")
    public void desativarUsuario(@PathVariable Long id) {
        userService.desativarUsuario(id);
    }
}
