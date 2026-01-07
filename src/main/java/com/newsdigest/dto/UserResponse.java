package com.newsdigest.dto;

import com.newsdigest.domain.User;

import java.time.LocalTime;

public class UserResponse {

    private Long id;
    private String email;
    private boolean ativo;
    private LocalTime horarioEnvio;

    public UserResponse(Long id, String email, boolean ativo, LocalTime horarioEnvio) {
        this.id = id;
        this.email = email;
        this.ativo = ativo;
        this.horarioEnvio = horarioEnvio;
    }

    public static UserResponse fromEntity(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.isAtivo(), user.getHorarioEnvio());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public LocalTime getHorarioEnvio() {
        return horarioEnvio;
    }
}
