package com.newsdigest.domain;

import jakarta.persistence.*;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "user_subscriptions",uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "news_source_id"})})
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "news_source_id", nullable = false)
    private NewsSource newsSource;

    @Column(nullable = false)
    private int quantidadeNoticias;

    protected UserSubscription() {
    }

    public UserSubscription(User user, NewsSource newsSource, int quantidadeNoticias) {
        if (quantidadeNoticias < 1 || quantidadeNoticias > 5) {
            throw new IllegalArgumentException("QuantidadeNoticias deve estar entre 1 e 5");
        }
        this.user = user;
        this.newsSource = newsSource;
        this.quantidadeNoticias = quantidadeNoticias;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public NewsSource getNewsSource() {
        return newsSource;
    }

    public NewsSource getSource() {
        return this.newsSource;
    }

    public int getQuantidadeNoticias() {
        return quantidadeNoticias;
    }

    public void setQuantidadeNoticias(int quantidadeNoticias) {
        if (quantidadeNoticias < 1 || quantidadeNoticias > 5) {
            throw new IllegalArgumentException("QuantidadeNoticias deve estar entre 1 e 5");
        }
        this.quantidadeNoticias = quantidadeNoticias;
    }
}

