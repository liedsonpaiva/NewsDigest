package com.newsdigest.repository;

import com.newsdigest.domain.NewsSource;
import com.newsdigest.domain.User;
import com.newsdigest.domain.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    //Futura requisições

    // Buscar todas as inscrições de um usuário
    List<UserSubscription> findByUser(User user);

    // Buscar todas as inscrições de uma fonte
    // List<UserSubscription> findByNewsSource(NewsSource newsSource);

    // Buscar inscrição específica (usuário + fonte)
    Optional<UserSubscription> findByUserAndNewsSource(User user, NewsSource newsSource);

    // Buscar usuários que devem receber notícias agora
    // @Query("SELECT us FROM UserSubscription us WHERE us.user.ativo = true AND us.user.horarioEnvio <= :agora")
    // List<UserSubscription> findUsuariosParaEnvio(@Param("agora") LocalTime agora);

    // Contar inscrições de um usuário
    // int countByUser(User user);

    // Contar quantos usuários seguem uma fonte
    // int countByNewsSource(NewsSource newsSource);


}
