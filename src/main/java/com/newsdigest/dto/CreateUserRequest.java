package com.newsdigest.dto;

import java.time.LocalTime;

public class CreateUserRequest {

    private String email;
    private LocalTime horarioEnvio;

    public CreateUserRequest() {
    }

    public String getEmail() {
        return email;
    }

    public LocalTime getHorarioEnvio() {
        return horarioEnvio;
    }
}
