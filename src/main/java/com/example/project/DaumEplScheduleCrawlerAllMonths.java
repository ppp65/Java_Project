package com.example.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DaumEplScheduleCrawlerAllMonths {

    public List<MatchDto> getSchedule() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        List<MatchDto> allMatches = new ArrayList<>();

        String[] months = {"202408", "202409", "202410", "202411", "202412", "202501", "202502", "202503", "202504", "202505"};

        try {
            for (String month : months) {
                String baseUrl = "https://sports.daum.net/schedule/epl?date=" + month;
                driver.get(baseUrl);

                // 페이지 끝까지 스크롤
                JavascriptExecutor js = (JavascriptExecutor) driver;
                long lastHeight = (long) js.executeScript("return document.body.scrollHeight");

                while (true) {
                    js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                    Thread.sleep(2000);
                    long newHeight = (long) js.executeScript("return document.body.scrollHeight");
                    if (newHeight == lastHeight) {
                        break;
                    }
                    lastHeight = newHeight;
                }

                // 경기 정보를 가져오는 부분
                WebElement tbodyElement = driver.findElement(By.id("scheduleList"));
                List<WebElement> matchElements = tbodyElement.findElements(By.tagName("tr"));
                String currentDate = "";

                for (WebElement match : matchElements) {
                    if (!match.findElements(By.cssSelector("td.td_date")).isEmpty()) {
                        currentDate = match.findElement(By.cssSelector("td.td_date span.num_date")).getText();
                    }

                    if (match.findElements(By.cssSelector("td.td_empty")).size() > 0) {
                        continue;
                    }

                    String time = match.findElement(By.cssSelector("td.td_time")).getText();
                    String stadium = match.findElement(By.cssSelector("td.td_area")).getText().trim();

                    WebElement homeTeamElement = match.findElement(By.cssSelector("div.team_home"));
                    String homeTeam = homeTeamElement.findElement(By.cssSelector("span.txt_team")).getText();
                    String homeTeamIcon = homeTeamElement.findElement(By.cssSelector("span.wrap_thumb img")).getAttribute("src");

                    WebElement awayTeamElement = match.findElement(By.cssSelector("div.team_away"));
                    String awayTeam = awayTeamElement.findElement(By.cssSelector("span.txt_team")).getText();
                    String awayTeamIcon = awayTeamElement.findElement(By.cssSelector("span.wrap_thumb img")).getAttribute("src");

                    String matchStatus = match.findElement(By.cssSelector("span.state_game")).getText();
                    String score = "경기 전";
                    if (matchStatus.equals("종료")) {
                        String homeScore = match.findElement(By.cssSelector("div.team_home em.num_score")).getText();
                        String awayScore = match.findElement(By.cssSelector("div.team_away em.num_score")).getText();
                        score = homeScore + " : " + awayScore;
                    }

                    // MatchDto 객체로 추가
                    allMatches.add(new MatchDto(currentDate, time, stadium, homeTeam, homeTeamIcon, score, awayTeam, awayTeamIcon));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return allMatches; // 크롤링한 경기 일정 리스트 반환
    }
}
