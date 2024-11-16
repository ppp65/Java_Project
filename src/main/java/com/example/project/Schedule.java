package com.example.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Schedule {

    public List<MatchDto> executeCrawling() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        List<MatchDto> matchList = new ArrayList<>();

        String[] months = {
                "202408", "202409", "202410", "202411", "202412",
                "202501", "202502", "202503", "202504", "202505"
        };

        try {
            for (String month : months) {
                String baseUrl = "https://sports.daum.net/schedule/epl?date=" + month;
                driver.get(baseUrl);
                scrollToBottom(driver);

                List<WebElement> matchElements = driver.findElements(By.cssSelector("#scheduleList tr"));
                String currentDate = "";
                String year = month.substring(0, 4); // 연도 추출

                for (WebElement match : matchElements) {
                    try {
                        if (!match.findElements(By.cssSelector("td.td_date")).isEmpty()) {
                            String dateText = match.findElement(By.cssSelector("td.td_date span.num_date")).getText();
                            currentDate = formatFullDate(year, dateText); // '2024-08-01' 형식으로 변환
                        }

                        if (match.findElements(By.cssSelector("td.td_empty")).size() > 0) {
                            continue;
                        }

                        String time = match.findElement(By.cssSelector("td.td_time")).getText();
                        if (time == null || !time.matches("\\d{2}:\\d{2}")) {
                            System.err.println("잘못된 시간 형식: " + time);
                            time = "00:00"; // 기본값 설정
                        }
                        //System.out.println("크롤링된 시간: " + time);

                        String stadium = match.findElement(By.cssSelector("td.td_area")).getText().trim();
                        String homeTeam = match.findElement(By.cssSelector("div.team_home span.txt_team")).getText();
                        String awayTeam = match.findElement(By.cssSelector("div.team_away span.txt_team")).getText();

                        String matchStatus = match.findElement(By.cssSelector("span.state_game")).getText();
                        String score = "경기 전";
                        if (matchStatus.equals("종료")) {
                            String homeScore = match.findElement(By.cssSelector("div.team_home em.num_score")).getText();
                            String awayScore = match.findElement(By.cssSelector("div.team_away em.num_score")).getText();
                            score = homeScore + " : " + awayScore;
                        }

                        MatchDto matchData = new MatchDto(currentDate, time, stadium, homeTeam, score, awayTeam);
                        matchList.add(matchData);
                    } catch (Exception e) {
                        System.err.println("경기 데이터를 처리 중 오류 발생: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

            }
        } catch (Exception e) {
            System.err.println("크롤링 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return matchList;
    }

    private void scrollToBottom(WebDriver driver) throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        long lastHeight = (long) js.executeScript("return document.body.scrollHeight");

        while (true) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(2000); // 스크롤 후 대기
            long newHeight = (long) js.executeScript("return document.body.scrollHeight");
            if (newHeight == lastHeight) {
                break;
            }
            lastHeight = newHeight;
        }
    }

    private String formatFullDate(String year, String dateText) {
        String[] parts = dateText.split("\\.");
        String month = parts[0].trim();
        String day = parts[1].trim();
        return year + "-" + (month.length() == 1 ? "0" + month : month) + "-" + (day.length() == 1 ? "0" + day : day);
    }
}
