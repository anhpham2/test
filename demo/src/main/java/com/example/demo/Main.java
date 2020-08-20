package com.example.demo;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("https://www.dota2protracker.com/")
//                .method("GET", null)
//                .addHeader("authority", "www.dota2protracker.com")
//                .addHeader("cache-control", "max-age=0")
//                .addHeader("upgrade-insecure-requests", "1")
//                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36")
//                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
//                .addHeader("sec-fetch-site", "none")
//                .addHeader("sec-fetch-mode", "navigate")
//                .addHeader("sec-fetch-user", "?1")
//                .addHeader("sec-fetch-dest", "document")
//                .addHeader("accept-language", "en-US,en;q=0.9")
//                .addHeader("cookie", "__cfduid=d66f7b79df5af24aa960131e42d9791cc1597907135")
//                .addHeader("if-modified-since", "Thu, 20 Aug 2020 07:03:32 GMT")
//                .build();
//        Response response = client.newCall(request).execute();
//        System.out.println(response.body().hashCode());
//        System.out.println(response.toString());
        String url = "http://dota2protracker.com/";
        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";
        Document document = Jsoup.connect(url).userAgent(USER_AGENT).get();
        Elements matchElements = document.select("table#table_recently_finished tbody tr");
        long matchId = 0L;
        int heroId = 0;
        Map<Integer, String> heroPlayerMap = new HashMap<>();
        Map<Long, Long> matchNeed2CheckFromODMap = new HashMap<>();
        for (Element matchElement : matchElements) {
            matchId = 0L;
            heroPlayerMap.clear();
            try {
                Element playersElement = matchElement.selectFirst("td.padding-left");
                if(playersElement != null){
                    heroId = 0;
                    for(Node node : playersElement.childNodes()) {
                        if (node instanceof Element) {
                            Element elementTmp = ((Element) node);
                            if(elementTmp.child(0) != null && elementTmp.child(0).tagName().equals("img")){
                                String heroName = elementTmp.attr("title");
                                if(!StringUtils.isEmpty(heroName)){
                                    heroId = 7;
                                }
                            }
                            //
                            if(elementTmp.child(0) != null && elementTmp.child(0).tagName().equals("a")){
                                if(heroId != 0){
                                    heroPlayerMap.put(heroId, elementTmp.child(0).text());
                                    heroId = 0;
                                }
                            }
                        }
                    }
                }
                Element infoElement = matchElement.selectFirst("td a.info");
                if(infoElement != null){
                    matchId = Long.parseLong(infoElement.attr("href").trim().replace("https://stratz.com/match/",""));
                }

                if(matchId != 0L && !heroPlayerMap.isEmpty()){
                    matchNeed2CheckFromODMap.put(matchId, matchId);
                }
                Thread.sleep(100);
            }catch (Exception e) {
                String error = "Error when reading element: " + e.getMessage();

            }
        }
        System.out.println("Found "+ matchNeed2CheckFromODMap.size()+ " game(s) From Dota2ProTracker");
    }
}
