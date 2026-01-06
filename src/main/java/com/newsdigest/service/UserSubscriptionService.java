package com.newsdigest.service;

import com.newsdigest.domain.NewsSource;
import com.newsdigest.domain.User;
import com.newsdigest.domain.UserSubscription;
import com.newsdigest.repository.UserSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserSubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    public UserSubscriptionService(UserSubscriptionRepository userSubscriptionRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    public UserSubscription inscreverUsuario(User user, NewsSource newsSource, int quantidadeNoticias) {
        Optional<UserSubscription> existente = userSubscriptionRepository.findByUserAndNewsSource(user, newsSource);
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Usuário já inscrito nesta fonte");
        }

        UserSubscription subscription = new UserSubscription(user, newsSource, quantidadeNoticias);

        return userSubscriptionRepository.save(subscription);
    }

    public void alterarQuantidadeNoticias(User user, NewsSource newsSource, int novaQuantidade) {
        UserSubscription subscription = userSubscriptionRepository.findByUserAndNewsSource(user, newsSource).orElseThrow(() -> new IllegalArgumentException("Inscrição não encontrada"));
        subscription.setQuantidadeNoticias(novaQuantidade);
        userSubscriptionRepository.save(subscription);
    }

    public List<UserSubscription> listarInscricoesDoUsuario(User user) {
        return userSubscriptionRepository.findByUser(user);
    }

    public void removerInscricao(User user, NewsSource newsSource) {
        UserSubscription subscription = userSubscriptionRepository.findByUserAndNewsSource(user, newsSource).orElseThrow(() -> new IllegalArgumentException("Inscrição não encontrada"));
        userSubscriptionRepository.delete(subscription);
    }
}
