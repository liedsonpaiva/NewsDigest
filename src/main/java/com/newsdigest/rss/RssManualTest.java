//package com.newsdigest.rss;
//
//import com.newsdigest.domain.NewsSource;
//import com.newsdigest.rss.dto.RssItem;
//
//import java.util.List;
//
//public class RssManualTest {
//
//    public static void main(String[] args) {
//
//        // 1. RSS real
//        String rssUrl = "";
//
//        // 2. Fonte fake (sem banco)
//        NewsSource source = new NewsSource("BBC News", rssUrl, true);
//
//        // 3. Instanciar manualmente (SEM Spring)
//        RssClient client = new RssClient();
//        RssParser parser = new RssParser();
//        RssService service = new RssService(client, parser);
//
//        // 4. Chamar o serviço
//        List<RssItem> news = service.fetchNews(source, 3);
//
//        // 5. Verificar resultado
//        for (RssItem item : news) {
//            System.out.println("Título: " + item.getTitle());
//            System.out.println("Link: " + item.getLink());
//            System.out.println("Imagem: " + item.getImageUrl());
//            System.out.println("----------");
//        }
//    }
//}
