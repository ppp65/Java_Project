package com.example.project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ProjectApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<String[]> rankings = EplDataCrawler.getEplRankings();

        // 크롤링 데이터 디버깅 출력
        for (String[] row : rankings) {
            System.out.println(Arrays.toString(row));  // 각 팀의 정보를 콘솔에 출력
        }

        EplDataInserter.insertDataToHtml(rankings);
        System.out.println("HTML 파일이 성공적으로 업데이트되었습니다.");
    }
}
