package com.example.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.project.DBService;
import java.util.List;

@SpringBootApplication
public class ProjectApplication implements CommandLineRunner {

    @Autowired
    private EplDataCrawler eplDataCrawler; // 순위 크롤러

    @Autowired
    private EplDataInserter eplDataInserter; // 순위 HTML 업데이트

    @Autowired
    private Schedule schedule; // 일정 크롤러

    @Autowired
    private SkySportsEplCrawlerWithMore skySportsEplCrawlerWithMore; // 뉴스 크롤러

    @Autowired
    private DBService dbService; // DB 저장 서비스

    @Autowired
    private SpotvEplHighlightsCrawler spotvEplHighlightsCrawler; // SPOTV 하이라이트 크롤러

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. 순위 크롤러 실행
        List<String[]> rankings = eplDataCrawler.getEplRankings();
        eplDataInserter.insertDataToHtml(rankings);
        System.out.println("순위 데이터 HTML 파일이 성공적으로 업데이트되었습니다.");

        // 2. 뉴스 크롤러 실행
        System.out.println("뉴스 크롤링 시작...");
        List<NewsDto> newsData = skySportsEplCrawlerWithMore.executeCrawling();

        // DB 업로드
        dbService.uploadNewsDataToOracle(newsData);
        System.out.println("뉴스 데이터가 성공적으로 처리되었습니다.");

        // 3. 일정 크롤링 실행 및 DB 저장
        System.out.println("일정 크롤링 시작...");
        List<MatchDto> matches = schedule.executeCrawling(); // 일정 데이터 크롤링

        dbService.uploadMatchDataToOracle(matches); // DB 저장
        System.out.println("일정 데이터가 성공적으로 처리되었습니다.");

        // 4. SPOTV 하이라이트 크롤러 실행
        System.out.println("SPOTV EPL 하이라이트 크롤링 시작...");
        spotvEplHighlightsCrawler.executeCrawling();
        System.out.println("SPOTV EPL 하이라이트 데이터가 성공적으로 크롤링되었습니다.");
    }
}
