package com.example.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class Schedule {

    public List<MatchDto> executeCrawling() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        List<MatchDto> matchList = new ArrayList<>();

        String filePath = Paths.get("src/main/resources/static/schedule.html").toAbsolutePath().toString();
        String[] months = {"202408", "202409", "202410", "202411", "202412", "202501", "202502", "202503", "202504", "202505"};

        StringBuilder tableContent = new StringBuilder();

        try {
            for (String month : months) {
                String baseUrl = "https://sports.daum.net/schedule/epl?date=" + month;
                driver.get(baseUrl);

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

                    String time = match.findElement(By.cssSelector("td.td_time")).getText();
                    String stadium = match.findElement(By.cssSelector("td.td_area")).getText().trim();

                    WebElement homeTeamElement = match.findElement(By.cssSelector("div.team_home"));
                    String homeTeamName = homeTeamElement.findElement(By.cssSelector("span.txt_team")).getText();

                    WebElement awayTeamElement = match.findElement(By.cssSelector("div.team_away"));
                    String awayTeamName = awayTeamElement.findElement(By.cssSelector("span.txt_team")).getText();

                    String matchStatus = match.findElement(By.cssSelector("span.state_game")).getText();
                    String score = "경기 전";
                    if (matchStatus.equals("종료")) {
                        String homeScore = match.findElement(By.cssSelector("div.team_home em.num_score")).getText();
                        String awayScore = match.findElement(By.cssSelector("div.team_away em.num_score")).getText();
                        score = homeScore + " : " + awayScore;
                    }

                    String homeTeam = homeTeamElement.getText();
                    String awayTeam = awayTeamElement.getText();

                    MatchDto match_data = new MatchDto(currentDate, time, stadium, homeTeamName, homeTeam, score, awayTeamName, awayTeam);
                    matchList.add(match_data);

                    if (!match.findElements(By.cssSelector("td.td_date")).isEmpty()) {
                        tableContent.append("<tr><td rowspan=\"" + rowspan + "\">" + currentDate + "</td><td>" + time + "</td><td>" + stadium + "</td><td>" + homeTeamName + "</td><td>" + score + "</td><td>" + awayTeamName + "</td></tr>");
                    } else {
                        tableContent.append("<tr><td>" + time + "</td><td>" + stadium + "</td><td>" + homeTeamName + "</td><td>" + score + "</td><td>" + awayTeamName + "</td></tr>");
                    }
                }
            }
            updateHtmlFile(filePath, tableContent.toString());
            System.out.println("EPL 전체 시즌 일정 크롤링 완료: " + filePath);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
        return matchList;
    }

    private static void updateHtmlFile(String filePath, String tableContent) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            boolean insideScheduleTable = false;

            for (String line : lines) {
                // `<tbody id="schedule-table">` 부분을 찾으면 기존 데이터를 덮어쓸 준비를 함
                if (line.contains("<tbody id=\"schedule-table\">")) {
                    writer.write(line);
                    writer.newLine();
                    writer.write(tableContent); // 새롭게 크롤링한 데이터를 추가
                    writer.newLine();
                    insideScheduleTable = true;
                } else if (line.contains("</tbody>") && insideScheduleTable) {
                    insideScheduleTable = false; // `</tbody>` 태그까지 추가
                    writer.write(line);
                    writer.newLine();
                } else if (!insideScheduleTable) {
                    writer.write(line); // `<tbody>` 외부의 다른 HTML 내용은 그대로 유지
                    writer.newLine();
                }
            }
        }
    }

}
