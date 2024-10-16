package com.example.project;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class EplRankingCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EplRankingCrawlerApplication.class, args);
    }

    public static void crawling() {
    }

    @Bean
    CommandLineRunner crawlerRunner() {
        return args -> {
            try {
                // 네이버 스포츠 EPL 순위 페이지 URL
                String url = "https://sports.news.naver.com/wfootball/record/index?category=epl&year=2024";

                // Jsoup으로 HTML 문서 가져오기
                Document doc = Jsoup.connect(url).get();

                // 스크립트 태그에서 JSON 데이터를 포함한 부분 찾기
                Elements scriptElements = doc.select("script");

                String jsonData = null;
                for (Element element : scriptElements) {
                    if (element.data().contains("jsonTeamRecord")) {
                        // jsonTeamRecord 라는 데이터가 포함된 스크립트를 찾고, 해당 부분에서 JSON 추출
                        String data = element.data();
                        int startIndex = data.indexOf("jsonTeamRecord:") + 16;
                        int endIndex = data.indexOf("}]", startIndex) + 2;
                        jsonData = data.substring(startIndex, endIndex);
                        break;
                    }
                }

                if (jsonData != null) {
                    System.out.println("JSON 데이터를 성공적으로 가져왔습니다: \n" + jsonData);
                    // JSON 데이터를 파싱하여 출력하는 로직을 여기에 추가하면 됩니다.
                } else {
                    System.out.println("JSON 데이터를 찾지 못했습니다.");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}
