package com.newsdigest.rss;

import com.newsdigest.domain.NewsSource;
import com.newsdigest.rss.dto.RssItem;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Service
public class RssService {

    private final RssClient client;
    private final RssParser parser;

    public RssService(RssClient client, RssParser parser) {
        this.client = client;
        this.parser = parser;
    }

    public List<RssItem> fetchNews(NewsSource source, int limit) {

        if (!source.isAtivo() || limit <= 0) {
            return Collections.emptyList();
        }

        try {
            InputStream xml = client.fetch(source.getRssUrl());
            List<RssItem> items = parser.parse(xml);

            return items.stream()
                    .limit(limit)
                    .toList();

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
