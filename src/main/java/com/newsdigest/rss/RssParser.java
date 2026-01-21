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
    private static final int MAX_DESCRIPTION_LENGTH = 500; // tamanho máximo do resumo

    public List<RssItem> parse(InputStream xml) {
        try (InputStream input = xml) {

            SyndFeed feed = new SyndFeedInput().build(new XmlReader(input));
            List<RssItem> items = new ArrayList<>();

            for (SyndEntry entry : feed.getEntries()) {

                if (entry.getTitle() == null || entry.getLink() == null) {
                    continue;
                }

                String image = extractImage(entry);
                String description = extractDescription(entry);

                items.add(new RssItem(
                        entry.getTitle(),
                        entry.getLink(),
                        image,
                        description
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

    private String extractDescription(SyndEntry entry) {
        if (entry.getDescription() == null) return "";

        String full = entry.getDescription().getValue();

        // Remove qualquer tag <img>, <video>, <iframe> que possa aparecer
        full = full.replaceAll("(?i)<(img|video|iframe)[^>]*>", "");

        // Pega apenas o primeiro parágrafo real
        String[] paragraphs = full.split("</p>|\\n|\\r"); // divide por parágrafo ou quebra de linha
        for (String p : paragraphs) {
            p = p.replaceAll("<[^>]+>", "").trim(); // limpa HTML residual
            if (p.length() > 500) { // pega parágrafo com conteúdo “real”
                full = p;
                break;
            }
        }

        // Limita o tamanho
        if (full.length() > MAX_DESCRIPTION_LENGTH) {
            full = full.substring(0, MAX_DESCRIPTION_LENGTH).trim() + "...";
        }

        return full;
    }
}
