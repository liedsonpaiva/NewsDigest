package com.newsdigest.dto;

public class CreateNewsSourceRequest {

    private Long id;
    private String nome;
    private String rssUrl;
    private String logoUrl; // NOVO

    public CreateNewsSourceRequest() {}

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getRssUrl() {
        return rssUrl;
    }

    public String getLogoUrl() {
        return logoUrl; }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl; }
}
