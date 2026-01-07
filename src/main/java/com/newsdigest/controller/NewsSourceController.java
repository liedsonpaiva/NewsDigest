package com.newsdigest.controller;

import com.newsdigest.domain.NewsSource;
import com.newsdigest.dto.CreateNewsSourceRequest;
import com.newsdigest.dto.NewsSourceResponse;
import com.newsdigest.repository.NewsSourceRepository;
import com.newsdigest.service.NewsSourceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news-sources")
public class NewsSourceController {

    private final NewsSourceService newsSourceService;

    public NewsSourceController(NewsSourceService newsSourceService) {
        this.newsSourceService = newsSourceService;
    }

    @GetMapping
    public List<NewsSourceResponse> listarFontesAtivas() {
        return newsSourceService.listarFontesAtivas().stream().map(NewsSourceResponse::fromEntity).toList();
    }

    @GetMapping("/{id}")
    public NewsSourceResponse buscarPorId(@PathVariable Long id) {
        NewsSource source = newsSourceService.buscarPorId(id);
        return NewsSourceResponse.fromEntity(source);
    }

    @PostMapping
    public NewsSourceResponse criarFonte(@RequestBody CreateNewsSourceRequest request) {
        NewsSource source = newsSourceService.criarFonte(request.getNome(), request.getRssUrl());
        return NewsSourceResponse.fromEntity(source);
    }
}
