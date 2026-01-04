package com.newsdigest.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "ativo", nullable = false)
    private boolean ativo;

    @Column(name = "horario_envio", nullable = false)
    private LocalTime horarioEnvio;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDate dataCadastro;

    protected User() {
    }

    public User(String email, boolean ativo, LocalTime horarioEnvio) {
        this.email = email;
        this.ativo = ativo;
        this.horarioEnvio = horarioEnvio;
        this.dataCadastro = LocalDate.now();
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

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void setHorarioEnvio(LocalTime horarioEnvio) {
        this.horarioEnvio = horarioEnvio;
    }

}
