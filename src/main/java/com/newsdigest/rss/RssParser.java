package com.newsdigest.rss;

import com.newsdigest.rss.dto.RssItem;
import com.rometools.modules.mediarss.MediaEntryModule;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class RssParser {

    private static final String DEFAULT_IMAGE = "https://via.placeholder.com/300x200.png?text=News";

    public List<RssItem> parse(InputStream xml) {
        try (InputStream input = xml) {

            SyndFeed feed = new SyndFeedInput().build(new XmlReader(input));
            List<RssItem> items = new ArrayList<>();

            for (SyndEntry entry : feed.getEntries()) {

                if (entry.getTitle() == null || entry.getLink() == null) {
                    continue;
                }

                String image = extractImage(entry);

                items.add(new RssItem(
                        entry.getTitle(),
                        entry.getLink(),
                        image
                ));
            }

            return items;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao parsear RSS", e);
        }
    }

    private String extractImage(SyndEntry entry) {

        // 1. Media RSS (BBC, DW, etc.)
        Module module = entry.getModule(MediaEntryModule.URI);
        if (module instanceof MediaEntryModule media) {
            if (media.getMediaContents() != null && media.getMediaContents().length > 0) {
                return media.getMediaContents()[0].getReference().toString();
            }
        }

        // 2. Enclosure (fallback)
        if (entry.getEnclosures() != null && !entry.getEnclosures().isEmpty()) {
            return entry.getEnclosures().get(0).getUrl();
        }

        // 3. Placeholder final
        return DEFAULT_IMAGE;
    }
}
