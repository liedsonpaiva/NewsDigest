package com.newsdigest.controller;

import com.newsdigest.domain.NewsSource;
import com.newsdigest.dto.CreateNewsSourceRequest;
import com.newsdigest.dto.NewsSourceResponse;
import com.newsdigest.repository.NewsSourceRepository;
import org.springframework.http.HttpStatus;
import com.newsdigest.service.NewsSourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/news-sources")
public class NewsSourceController {

    private final NewsSourceService newsSourceService;
    private static final Logger log = LoggerFactory.getLogger(NewsSourceController.class);

    public NewsSourceController(NewsSourceService newsSourceService) {
        this.newsSourceService = newsSourceService;
    }

    @GetMapping
    public ResponseEntity<List<NewsSourceResponse>> listarFontesAtivas() {
        List<NewsSourceResponse> response = newsSourceService.listarFontesAtivas()
                .stream()
                .map(NewsSourceResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsSourceResponse> buscarPorId(@PathVariable Long id) {
        try {
            NewsSource source = newsSourceService.buscarPorId(id);
            return ResponseEntity.ok(NewsSourceResponse.fromEntity(source));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<NewsSourceResponse> criarFonte(@RequestBody CreateNewsSourceRequest request) {
        NewsSource source = newsSourceService.criarFonte(request.getNome(), request.getRssUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(NewsSourceResponse.fromEntity(source));
    }
}

