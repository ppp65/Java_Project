package com.example.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class DaumEplScheduleCrawler {

    public static void main(String[] args) {
        // WebDriver 설정
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        // Daum 스포츠 EPL 일정 페이지 URL
        String baseUrl = "https://sports.daum.net/schedule/epl";

        // 출력할 HTML 파일 경로
        String filePath = Paths.get("DaumEplSchedule.html").toAbsolutePath().toString();

        try {
            // HTML 파일 작성 시작
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // HTML 헤더 작성
                writer.write("<!DOCTYPE html><html lang=\"ko\"><head><meta charset=\"UTF-8\"><title>EPL 경기 일정</title>");
                writer.write("<style>");
                writer.write("table { width: 100%; border-collapse: collapse; }");
                writer.write("th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }");
                writer.write("th { background-color: #f2f2f2; }");
                writer.write("tr:nth-child(even) { background-color: #f9f9f9; }");
                writer.write("tr:hover { background-color: #f1f1f1; }");
                writer.write("</style>");
                writer.write("</head><body>");
                writer.write("<h1>Daum EPL 경기 일정</h1><table><tr><th>날짜</th><th>경기 시간</th><th>경기장</th><th>홈팀</th><th>스코어</th><th>원정팀</th></tr>");

                // 페이지 접속
                driver.get(baseUrl);

                // 페이지 끝까지 스크롤
                JavascriptExecutor js = (JavascriptExecutor) driver;
                long lastHeight = (long) js.executeScript("return document.body.scrollHeight");

                while (true) {
                    js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                    Thread.sleep(2000); // 페이지가 로드될 시간을 기다림
                    long newHeight = (long) js.executeScript("return document.body.scrollHeight");
                    if (newHeight == lastHeight) {
                        break; // 더 이상 스크롤이 변화가 없으면 탈출
                    }
                    lastHeight = newHeight;
                }

                // 경기 정보가 있는 tbody 요소 선택
                WebElement tbodyElement = driver.findElement(By.id("scheduleList"));

                // 각 경기를 나타내는 tr 요소들 선택
                List<WebElement> matchElements = tbodyElement.findElements(By.tagName("tr"));

                // 날짜 정보를 저장할 변수 (rowspan의 영향 때문에 필요)
                String currentDate = "";

                for (WebElement match : matchElements) {
                    // 날짜 정보가 rowspan에 걸려있을 경우, 새롭게 날짜를 추출
                    if (!match.findElements(By.cssSelector("td.td_date")).isEmpty()) {
                        currentDate = match.findElement(By.cssSelector("td.td_date span.num_date")).getText();
                    }

                    // 경기가 없는 행은 건너뜁니다
                    if (match.findElements(By.cssSelector("td.td_empty")).size() > 0) {
                        continue;
                    }

                    // 경기 시간
                    String time = match.findElement(By.cssSelector("td.td_time")).getText();

                    // 경기장
                    String stadium = match.findElement(By.cssSelector("td.td_area")).getText().trim();

                    // 홈팀 이름
                    String homeTeam = match.findElement(By.cssSelector("div.team_home span.txt_team")).getText();

                    // 원정팀 이름
                    String awayTeam = match.findElement(By.cssSelector("div.team_away span.txt_team")).getText();

                    // 경기 상태 및 스코어 확인
                    String matchStatus = match.findElement(By.cssSelector("span.state_game")).getText();
                    String score = "경기 전";
                    if (matchStatus.equals("종료")) {
                        String homeScore = match.findElement(By.cssSelector("div.team_home em.num_score")).getText();
                        String awayScore = match.findElement(By.cssSelector("div.team_away em.num_score")).getText();
                        score = homeScore + " : " + awayScore;
                    }

                    // HTML 테이블에 추가 (현재 날짜를 사용)
                    writer.write("<tr><td>" + currentDate + "</td><td>" + time + "</td><td>" + stadium + "</td><td>" + homeTeam + "</td><td>" + score + "</td><td>" + awayTeam + "</td></tr>");
                }

                // HTML 파일 마무리
                writer.write("</table></body></html>");
                System.out.println("EPL 일정 크롤링 완료: " + filePath);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // WebDriver 종료
            driver.quit();
        }
    }
}
