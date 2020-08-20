package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController1 {
    @RequestMapping(value = "/hi1")
    public ResponseEntity<String> getProduct() {
        String url = "http://dota2protracker.com/";
        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";
        Document document = null;
        try {
            document = Jsoup.connect(url).userAgent(USER_AGENT).get();
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
                //Thread.sleep(100);
            }catch (Exception e) {
                String error = "Error when reading element: " + e.getMessage();

            }
        }
        System.out.println("Found "+ matchNeed2CheckFromODMap.size()+ " game(s) From Dota2ProTracker");

        return new ResponseEntity<>("Found "+ matchNeed2CheckFromODMap.size()+ " game(s) From Dota2ProTracker" , HttpStatus.OK);
    }
}
