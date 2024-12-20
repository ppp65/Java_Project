package com.example.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class DaumEplScheduleCrawler10 {

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        String baseUrl = "https://sports.daum.net/schedule/epl?date=202410";

        String filePath = Paths.get("DaumEplSchedule10.html").toAbsolutePath().toString();

        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("<!DOCTYPE html><html lang=\"ko\"><head><meta charset=\"UTF-8\"><title>EPL 경기 일정</title>");
                writer.write("<style>");
                writer.write("table { width: 100%; border-collapse: collapse; }");
                writer.write("th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }");
                writer.write("th { background-color: #f2f2f2; }");
                writer.write("tr:nth-child(even) { background-color: #f9f9f9; }");
                writer.write("tr:hover { background-color: #f1f1f1; }");
                writer.write("img { width: 26px; height: 26px; vertical-align: middle; margin-right: 5px; }");
                writer.write("</style>");
                writer.write("</head><body>");
                writer.write("<h1>Daum EPL 경기 일정</h1><table><tr><th>날짜</th><th>경기 시간</th><th>경기장</th><th>홈팀</th><th>스코어</th><th>원정팀</th></tr>");

                driver.get(baseUrl);

                JavascriptExecutor js = (JavascriptExecutor) driver;
                long lastHeight = (long) js.executeScript("return document.body.scrollHeight");

                while (true) {
                    js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                    Thread.sleep(2000); // 페이지가 로드될 시간을 기다림
                    long newHeight = (long) js.executeScript("return document.body.scrollHeight");
                    if (newHeight == lastHeight) {
                        break;
                    }
                    lastHeight = newHeight;
                }

                WebElement tbodyElement = driver.findElement(By.id("scheduleList"));

                List<WebElement> matchElements = tbodyElement.findElements(By.tagName("tr"));

                String currentDate = "";
                int rowspan = 0;

                for (int i = 0; i < matchElements.size(); i++) {
                    WebElement match = matchElements.get(i);

                    if (!match.findElements(By.cssSelector("td.td_date")).isEmpty()) {
                        rowspan = 1;
                        for (int j = i + 1; j < matchElements.size(); j++) {
                            WebElement nextMatch = matchElements.get(j);
                            if (nextMatch.findElements(By.cssSelector("td.td_date")).isEmpty()) {
                                rowspan++;
                            } else {
                                break;
                            }
                        }
                        currentDate = match.findElement(By.cssSelector("td.td_date span.num_date")).getText();
                    }

                    if (match.findElements(By.cssSelector("td.td_empty")).size() > 0) {
                        continue;
                    }

                    // 경기 시간
                    String time = match.findElement(By.cssSelector("td.td_time")).getText();

                    // 경기장
                    String stadium = match.findElement(By.cssSelector("td.td_area")).getText().trim();

                    // 홈팀 정보 (팀 이름과 아이콘)
                    WebElement homeTeamElement = match.findElement(By.cssSelector("div.team_home"));
                    String homeTeamName = homeTeamElement.findElement(By.cssSelector("span.txt_team")).getText();
                    String homeTeamIcon = homeTeamElement.findElement(By.cssSelector("span.wrap_thumb img")).getAttribute("src");

                    // 원정팀 정보 (팀 이름과 아이콘)
                    WebElement awayTeamElement = match.findElement(By.cssSelector("div.team_away"));
                    String awayTeamName = awayTeamElement.findElement(By.cssSelector("span.txt_team")).getText();
                    String awayTeamIcon = awayTeamElement.findElement(By.cssSelector("span.wrap_thumb img")).getAttribute("src");

                    // 경기 상태 및 스코어 확인
                    String matchStatus = match.findElement(By.cssSelector("span.state_game")).getText();
                    String score = "경기 전";
                    if (matchStatus.equals("종료")) {
                        String homeScore = match.findElement(By.cssSelector("div.team_home em.num_score")).getText();
                        String awayScore = match.findElement(By.cssSelector("div.team_away em.num_score")).getText();
                        score = homeScore + " : " + awayScore;
                    }

                    // 첫 번째 경기에만 날짜 출력 (rowspan 사용)
                    if (!match.findElements(By.cssSelector("td.td_date")).isEmpty()) {
                        writer.write("<tr><td rowspan=\"" + rowspan + "\">" + currentDate + "</td><td>" + time + "</td><td>" + stadium + "</td><td><img src=\"" + homeTeamIcon + "\">" + homeTeamName + "</td><td>" + score + "</td><td><img src=\"" + awayTeamIcon + "\">" + awayTeamName + "</td></tr>");
                    } else {
                        // 같은 날짜인 나머지 경기는 날짜를 생략
                        writer.write("<tr><td>" + time + "</td><td>" + stadium + "</td><td><img src=\"" + homeTeamIcon + "\">" + homeTeamName + "</td><td>" + score + "</td><td><img src=\"" + awayTeamIcon + "\">" + awayTeamName + "</td></tr>");
                    }
                }

                writer.write("</table></body></html>");
                System.out.println("EPL 일정 크롤링 완료: " + filePath);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
