package com.newsdigest.dto;

public class CreateNewsSourceRequest {

    private String nome;
    private String rssUrl;

    public CreateNewsSourceRequest() {}

    public String getNome() {
        return nome;
    }

    public String getRssUrl() {
        return rssUrl;
    }
}
