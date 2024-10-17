package com.example.project;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EplDataCrawler {

    public List<String[]> getEplRankings() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        List<String[]> rankings = new ArrayList<>();

        try {
            driver.get("https://sports.daum.net/record/epl");

            List<WebElement> rows = driver.findElements(By.cssSelector(".tbl_record tbody tr"));
            for (WebElement row : rows) {
                List<WebElement> columns = row.findElements(By.tagName("td"));

                // 순위 숫자만 추출
                String rankAndNote = columns.get(0).getText();
                String rank = rankAndNote.split("\\s+")[0];  // 순위 숫자만 추출
                String note = getQualificationStatus(rankAndNote);  // 자격 상태 확인

                // 나머지 열 데이터 가져오기
                String team = columns.get(1).getText();
                String games = columns.get(2).getText();
                String wins = columns.get(3).getText();
                String draws = columns.get(4).getText();
                String losses = columns.get(5).getText();
                String goalsFor = columns.get(6).getText();
                String goalsAgainst = columns.get(7).getText();
                String goalDiff = columns.get(8).getText();
                String points = columns.get(9).getText();

                rankings.add(new String[]{rank, team, games, wins, draws, losses, goalsFor, goalsAgainst, goalDiff, points, note});
            }
        } finally {
            driver.quit();
        }
        return rankings;
    }

    // 순위에 포함된 자격 문구를 확인하는 메소드
    private String getQualificationStatus(String rankText) {
        if (rankText.contains("UEFA")) {
            return "UEFA 챔스 출전 자격";
        } else if (rankText.contains("유로파")) {
            return "유로파리그 출전 자격";
        } else if (rankText.contains("2부")) {
            return "2부 리그 강등";
        }
        return "";
    }
}
