package com.newsdigest.dto;

import java.time.LocalTime;

public class UpdateUserHorarioRequest {

    private LocalTime horarioEnvio;

    public UpdateUserHorarioRequest() {
    }

    public LocalTime getHorarioEnvio() {
        return horarioEnvio;
    }
}
