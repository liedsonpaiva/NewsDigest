package com.newsdigest.service;

import com.newsdigest.domain.NewsSource;
import com.newsdigest.domain.User;
import com.newsdigest.domain.UserSubscription;
import com.newsdigest.repository.NewsSourceRepository;
import com.newsdigest.repository.UserRepository;
import com.newsdigest.repository.UserSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserSubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserRepository userRepository;
    private final NewsSourceRepository newsSourceRepository;

    @Autowired
    public UserSubscriptionService(UserSubscriptionRepository userSubscriptionRepository, UserRepository userRepository, NewsSourceRepository newsSourceRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.userRepository = userRepository;
        this.newsSourceRepository = newsSourceRepository;
    }

    public void inscrever(Long userId, Long newsSourceId, int quantidadeNoticias) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (!user.isAtivo()) {throw new IllegalArgumentException("Usuário desativado. Não é possível realizar novas assinaturas.");}

        NewsSource source = newsSourceRepository.findById(newsSourceId).orElseThrow(() -> new IllegalArgumentException("Fonte não encontrada"));

        boolean jaExiste = userSubscriptionRepository.existsByUserAndNewsSource(user, source);

        if (jaExiste) {
            throw new IllegalArgumentException("Usuário já inscrito nesta fonte");
        }

        UserSubscription subscription = new UserSubscription(user, source, quantidadeNoticias);

        userSubscriptionRepository.save(subscription);
    }

    public List<UserSubscription> listar(Long userId) {
        return userSubscriptionRepository.findByUserId(userId);
    }

    @Transactional
    public void remover(Long userId, Long newsSourceId) {
        Optional<UserSubscription> subscription = userSubscriptionRepository.findByUserIdAndNewsSourceId(userId, newsSourceId);
        if (subscription.isPresent()) {
            userSubscriptionRepository.delete(subscription.get());
        } else {
            throw new IllegalArgumentException("Assinatura não encontrada");
        }
    }
}
