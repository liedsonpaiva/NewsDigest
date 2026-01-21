package com.newsdigest.service;

import com.newsdigest.domain.User;
import com.newsdigest.rss.dto.RssItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DigestTemplateBuilder {

    private static final String PLACEHOLDER_IMAGE = "https://via.placeholder.com/300x200.png?text=News";

    public String build(User user, Map<String, List<RssItem>> newsBySource) {

        StringBuilder html = new StringBuilder();

        html.append("<h1>NewsDigest</h1>");

        for (Map.Entry<String, List<RssItem>> entry : newsBySource.entrySet()) {

            String sourceName = entry.getKey();
            List<RssItem> items = entry.getValue();

            html.append("<h2>").append(sourceName).append("</h2>");

            if (items == null || items.isEmpty()) {
                html.append("<p>Sem notícias recentes</p>");
                continue;
            }

            for (RssItem item : items) {

                String imageUrl = item.getImageUrl() != null
                        ? item.getImageUrl()
                        : PLACEHOLDER_IMAGE;

                // Limpa imagens da descrição
                String description = cleanDescription(item.getDescription());

                html.append("<div style=\"margin-bottom:20px;\">");

                // Título preto e clicável
                html.append("<h3 style=\"color:black; font-size:16px; margin-bottom:5px;\"><a href=\"")
                        .append(item.getLink())
                        .append("\" target=\"_blank\" style=\"color:black; text-decoration:none;\">")
                        .append(item.getTitle())
                        .append("</a></h3>");

                // Imagem
                html.append("<img src=\"")
                        .append(imageUrl)
                        .append("\" width=\"300\" height=\"200\" style=\"display:block; margin-bottom:10px;\"/>");

                // Descrição limpa
                html.append("<p>").append(description).append("</p>");

                html.append("</div>");
            }
        }

        html.append("<hr>");
        html.append("<a href=\"http://localhost:8080/unsubscribe?token=")
                .append(user.getTokenCancelamento())
                .append("\">Descadastrar</a>");

        return html.toString();
    }

    // Remove todas as tags <img> da descrição
    private String cleanDescription(String description) {
        if (description == null) return "";
        return description.replaceAll("(?i)<img[^>]*>", "");
    }
}
