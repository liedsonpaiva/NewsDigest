package com.newsdigest.rss;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class RssClient {

    public InputStream fetch(String rssUrl) {
        try {
            URL url = new URL(rssUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("RSS retornou status HTTP: " + status);
            }

            return connection.getInputStream();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao baixar RSS: " + rssUrl, e);
        }
    }
}
