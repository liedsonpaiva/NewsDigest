package com.newsdigest.dto;

import com.newsdigest.domain.UserSubscription;

public class UserSubscriptionResponse {

    private Long newsSourceId;
    private String newsSourceName;
    private int quantidadeNoticias;

    public UserSubscriptionResponse(
            Long newsSourceId,
            String newsSourceName,
            int quantidadeNoticias
    ) {
        this.newsSourceId = newsSourceId;
        this.newsSourceName = newsSourceName;
        this.quantidadeNoticias = quantidadeNoticias;
    }

    public static UserSubscriptionResponse fromEntity(UserSubscription sub) {
        return new UserSubscriptionResponse(
                sub.getNewsSource().getId(),
                sub.getNewsSource().getNome(),
                sub.getQuantidadeNoticias()
        );
    }

    public Long getNewsSourceId() { return newsSourceId; }
    public String getNewsSourceName() { return newsSourceName; }
    public int getQuantidadeNoticias() { return quantidadeNoticias; }
}
