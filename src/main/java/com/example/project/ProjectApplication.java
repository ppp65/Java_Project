package com.example.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ProjectApplication implements CommandLineRunner {

    @Autowired
    private EplDataCrawler eplDataCrawler;           // 순위 크롤러

    @Autowired
    private EplDataInserter eplDataInserter;         // 순위 데이터를 HTML로 저장하는 서비스


    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. 순위 크롤러 실행
        List<String[]> rankings = eplDataCrawler.getEplRankings();

        // 순위 크롤링 데이터 출력
        for (String[] row : rankings) {
            System.out.println("순위 데이터: " + String.join(", ", row));
        }

        // 순위 데이터를 HTML 파일로 저장
        eplDataInserter.insertDataToHtml(rankings);
        System.out.println("순위 데이터 HTML 파일이 성공적으로 업데이트되었습니다.");


    }
}
