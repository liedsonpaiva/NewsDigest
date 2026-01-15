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

            html.append("<ul>");
            for (RssItem item : items) {

                String imageUrl = item.getImageUrl() != null
                        ? item.getImageUrl()
                        : PLACEHOLDER_IMAGE;

                html.append("<li>")
                        .append("<img src=\"").append(imageUrl)
                        .append("\" width=\"300\" height=\"200\">")
                        .append("<br>")
                        .append("<a href=\"").append(item.getLink())
                        .append("\" target=\"_blank\">")
                        .append(item.getTitle())
                        .append("</a>")
                        .append("</li>");
            }
            html.append("</ul>");
        }

        html.append("<hr>");
        html.append("<a href=\"https://newsdigest.com/unsubscribe?token=")
                .append(user.getTokenCancelamento())
                .append("\">Descadastrar</a>");

        return html.toString();
    }
}
