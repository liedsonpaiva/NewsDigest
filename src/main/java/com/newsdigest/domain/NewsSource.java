package com.newsdigest.domain;

import jakarta.persistence.*;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(
        name = "news_sources",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "rss_url")
        }
)
public class NewsSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //    id (long) [PK]

    @Column(nullable = false, length = 150)
    private String nome;//    nome (string) [NOT NULL]

    @Column(name = "rss_url", nullable = false)
    private String rssUrl; //    rssUrl (string) [NOT NULL] [UNIQUE]

    @Column(name = "ativo", nullable = false)
    private boolean ativo;

    protected NewsSource() {
    }

    public NewsSource(String nome, String rssUrl, boolean ativo) {
        this.nome = nome;
        this.rssUrl = rssUrl;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getRssUrl() {
        return rssUrl;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

}
