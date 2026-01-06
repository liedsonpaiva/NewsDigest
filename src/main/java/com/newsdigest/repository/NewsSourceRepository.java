package com.newsdigest.repository;

import com.newsdigest.domain.NewsSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsSourceRepository extends JpaRepository<NewsSource, Long> {

    //Futura requisições
    // Buscar todas as fontes ativas
    List<NewsSource> findByAtivoTrue();

    // Buscar fonte por nome
    // Optional<NewsSource> findByNome(String nome);

    // Buscar fonte por URL
    // Optional<NewsSource> findByRssUrl(String rssUrl);
}
