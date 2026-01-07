package com.newsdigest.repository;

import com.newsdigest.domain.NewsSource;
import com.newsdigest.domain.User;
import com.newsdigest.domain.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    //Futura requisições

    // Busca todas as inscrições de um usuário específico
    // SELECT * FROM user_subscription WHERE user_id = ?
    List<UserSubscription> findByUser(User user);

    // Busca todas as inscrições relacionadas a uma fonte de notícias
    // Útil para saber quais usuários seguem uma determinada fonte
    // SELECT * FROM user_subscription WHERE news_source_id = ?
    List<UserSubscription> findByNewsSource(NewsSource newsSource);

    // Busca uma inscrição específica combinando usuário + fonte
    // Usado para verificar se o usuário já está inscrito em uma fonte
    // SELECT * FROM user_subscription WHERE user_id = ? AND news_source_id = ?
    Optional<UserSubscription> findByUserAndNewsSource(User user, NewsSource newsSource);

    // ================================
    // VERIFICAÇÕES (BOOLEAN)
    // ================================

    // Verifica se já existe inscrição para um usuário em uma fonte
    // Retorna true ou false sem precisar carregar a entidade inteira
    // SELECT COUNT(*) > 0 FROM user_subscription WHERE user_id = ? AND news_source_id = ?
    boolean existsByUserAndNewsSource(User user, NewsSource newsSource);

    // ================================
    // CONSULTAS USANDO IDs
    // ================================

    // Busca inscrição usando apenas os IDs (mais comum em APIs REST)
    // Evita precisar carregar User e NewsSource antes
    // SELECT * FROM user_subscription WHERE user_id = ? AND news_source_id = ?
    Optional<UserSubscription> findByUserIdAndNewsSourceId(Long userId, Long newsSourceId);

    // Busca todas as inscrições de um usuário usando o ID
    // Muito útil para controllers REST
    // SELECT * FROM user_subscription WHERE user_id = ?
    List<UserSubscription> findByUserId(Long userId);

    // ================================
    // REMOÇÃO
    // ================================

    // Remove uma inscrição específica usando os IDs
    // DELETE FROM user_subscription WHERE user_id = ? AND news_source_id = ?
    void deleteByUserIdAndNewsSourceId(Long userId, Long newsSourceId);

    // ================================
    // CONTADORES
    // ================================

    // Conta quantas inscrições um usuário possui
    // Útil para limites de plano (ex: máximo de fontes)
    // SELECT COUNT(*) FROM user_subscription WHERE user_id = ?
    int countByUser(User user);

    // Conta quantos usuários seguem uma determinada fonte
    // Útil para métricas e popularidade
    // SELECT COUNT(*) FROM user_subscription WHERE news_source_id = ?
    int countByNewsSource(NewsSource newsSource);

    // ================================
    // CONSULTA AVANÇADA (FUTURA)
    // ================================

    /*
    // Busca usuários ativos que devem receber notícias no horário atual
    // Exemplo de uso com envio automático de notificações
    @Query("SELECT us FROM UserSubscription us " +
           "WHERE us.user.ativo = true " +
           "AND us.user.horarioEnvio <= :agora")
    List<UserSubscription> findUsuariosParaEnvio(@Param("agora") LocalTime agora);
    */


}
