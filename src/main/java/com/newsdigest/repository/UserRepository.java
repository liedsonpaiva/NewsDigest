package com.newsdigest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.newsdigest.domain.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar usuário por email
    Optional<User> findByEmail(String email);

    // Buscar todos os usuários ativos
    // List<User> findByAtivoTrue();

    // Buscar usuários ativos que devem receber notícias agora
    // @Query("SELECT u FROM User u WHERE u.ativo = true AND u.horarioEnvio <= :agora")
    // List<User> findUsuariosParaEnvio(@Param("agora") LocalTime agora);

    // Buscar usuários cadastrados depois de uma data específica
    // List<User> findByDataCadastroAfter(LocalDate data);

    List<User> findByAtivoTrueAndHorarioEnvioLessThanEqual(LocalTime now);

    Optional<User> findByTokenCancelamento(String token);

}
