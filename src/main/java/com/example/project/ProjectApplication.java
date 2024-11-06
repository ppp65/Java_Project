package com.example.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ProjectApplication implements CommandLineRunner {

    @Autowired
    private EplDataCrawler eplDataCrawler;

    @Autowired
    private EplDataInserter eplDataInserter;

    @Autowired
    private Schedule schedule;

    @Autowired
    private SkySportsEplCrawlerWithMore skySportsEplCrawlerWithMore;

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. 순위 크롤러 실행
        List<String[]> rankings = eplDataCrawler.getEplRankings();
        eplDataInserter.insertDataToHtml(rankings);
        System.out.println("순위 데이터 HTML 파일이 성공적으로 업데이트되었습니다.");

        // 2. 일정 크롤러 실행
        schedule.executeCrawling();

        // 3. SkySportsEplCrawlerWithMore 실행 (별도로 처리 필요 시 추가 작업)
        skySportsEplCrawlerWithMore.getNews();
    }
}
