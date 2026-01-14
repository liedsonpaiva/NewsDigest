package com.newsdigest.rss.dto;

public class RssItem {

    private String title;
    private String link;
    private String imageUrl;

    public RssItem(String title, String link, String imageUrl) {
        this.title = title;
        this.link = link;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
