package com.newsdigest.controller;

import com.newsdigest.domain.NewsSource;
import com.newsdigest.dto.ApiResponse;
import com.newsdigest.dto.CreateNewsSourceRequest;
import com.newsdigest.dto.NewsSourceResponse;
import com.newsdigest.service.NewsSourceService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<List<NewsSourceResponse>>> listarFontesAtivas() {

        List<NewsSourceResponse> response = newsSourceService.listarFontesAtivas()
                .stream()
                .map(NewsSourceResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Lista de fontes ativas",
                        response
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsSourceResponse>> buscarPorId(@PathVariable Long id) {

        try {
            NewsSource source = newsSourceService.buscarPorId(id);

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Fonte encontrada",
                            NewsSourceResponse.fromEntity(source)
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    ));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NewsSourceResponse>> criarFonte(
            @RequestBody CreateNewsSourceRequest request) {

        NewsSource source = newsSourceService.criarFonte(
                request.getId(),
                request.getNome(),
                request.getRssUrl(),
                request.getLogoUrl()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Fonte criada com sucesso", NewsSourceResponse.fromEntity(source)));
    }
}
