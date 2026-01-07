package com.newsdigest.dto;

import com.newsdigest.domain.NewsSource;

public class NewsSourceResponse {

    private Long id;
    private String nome;
    private String rssUrl;
    private boolean ativo;

    public NewsSourceResponse(Long id, String nome, String rssUrl, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.rssUrl = rssUrl;
        this.ativo = ativo;
    }

    public static NewsSourceResponse fromEntity(NewsSource source) {
        return new NewsSourceResponse(
                source.getId(),
                source.getNome(),
                source.getRssUrl(),
                source.isAtivo()
        );
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getRssUrl() { return rssUrl; }
    public boolean isAtivo() { return ativo; }
}
