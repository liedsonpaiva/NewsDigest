package com.newsdigest.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "users",uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
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

    @Column(name = "data_ultimo_envio_digest")
    private LocalDate dataUltimoEnvioDigest;

    @Column(name = "token_de_cancelamento", unique = true)
    private String tokenCancelamento;

    protected User() {
    }

    public User(String email, boolean ativo, LocalTime horarioEnvio) {
        this.email = email;
        this.ativo = ativo;
        this.horarioEnvio = horarioEnvio;
        this.dataCadastro = LocalDate.now();
        this.dataUltimoEnvioDigest = null;
        this.tokenCancelamento = UUID.randomUUID().toString();
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

    public LocalDate getDataUltimoEnvioDigest() {
        return dataUltimoEnvioDigest;
    }

    public String getTokenCancelamento() {
        return tokenCancelamento;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void setHorarioEnvio(LocalTime horarioEnvio) {
        this.horarioEnvio = horarioEnvio;
    }

    public void setDataUltimoEnvioDigest(LocalDate dataUltimoEnvioDigest) {
        this.dataUltimoEnvioDigest = dataUltimoEnvioDigest;
    }

}
