package com.newsdigest.dto;

import com.newsdigest.domain.NewsSource;

public class NewsSourceResponse {

    private Long id;
    private String name;
    private boolean active;
    private String logoUrl;

    public static NewsSourceResponse fromEntity(NewsSource source) {
        NewsSourceResponse dto = new NewsSourceResponse();
        dto.id = source.getId();
        dto.name = source.getNome();
        dto.active = source.isAtivo();
        dto.logoUrl = source.getLogoUrl();
        return dto;
    }

    // GETTERS OBRIGATÓRIOS para o JSON
    public Long getId() { return id; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
    public String getLogoUrl() { return logoUrl; }

    // Setters (opcionais)
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setActive(boolean active) { this.active = active; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
}
