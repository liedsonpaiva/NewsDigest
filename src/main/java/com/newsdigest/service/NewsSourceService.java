package com.newsdigest.service;

import com.newsdigest.domain.NewsSource;
import com.newsdigest.repository.NewsSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsSourceService {

    private final NewsSourceRepository newsSourceRepository;

    @Autowired
    public NewsSourceService(NewsSourceRepository newsSourceRepository) {
        this.newsSourceRepository = newsSourceRepository;
    }

    public List<NewsSource> listarFontesAtivas() {
        return newsSourceRepository.findByAtivoTrue();
    }

    public NewsSource buscarPorId(Long id) {
        return newsSourceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Fonte não encontrada"));
    }

    public NewsSource criarFonte(Long id, String nome, String rssUrl, String logoUrl) {
        NewsSource source = new NewsSource(id, nome, rssUrl, true, logoUrl);
        return newsSourceRepository.save(source);
    }

}
