package com.newsdigest.dto;

public class CreateUserSubscriptionRequest {
    private Long newsSourceId;
    private int quantidadeNoticias;

    public Long getNewsSourceId() {
        return newsSourceId;
    }

    public int getQuantidadeNoticias() {
        return quantidadeNoticias;
    }
}
