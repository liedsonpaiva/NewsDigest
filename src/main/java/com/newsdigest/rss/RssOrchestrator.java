package com.newsdigest.rss;

import com.newsdigest.domain.NewsSource;
import com.newsdigest.rss.dto.RssItem;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RssOrchestrator {

    private final RssService rssService;

    public RssOrchestrator(RssService rssService) {
        this.rssService = rssService;
    }

    public Map<NewsSource, List<RssItem>> fetchAll(List<NewsSource> sources, int limitPerSource) {
        Map<NewsSource, List<RssItem>> result = new HashMap<>();

        for (NewsSource source : sources) {
            if (!source.isAtivo()) continue;

            try {
                List<RssItem> items = rssService.fetchNews(source, limitPerSource);
                result.put(source, items);
            } catch (Exception e) {
                result.put(source, List.of());
            }
        }

        return result;
    }
}