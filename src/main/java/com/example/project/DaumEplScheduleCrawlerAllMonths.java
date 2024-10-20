package com.example.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class DaumEplScheduleCrawlerAllMonths {

    public static void main(String[] args) {
        // WebDriver 설정
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        // 출력할 HTML 파일 경로
        String filePath = Paths.get("DaumEplFullSeasonSchedule.html").toAbsolutePath().toString();

        // 2024-2025 시즌 동안 8월부터 5월까지의 데이터를 가져오기 위한 월별 URL 생성
        String[] months = {"202408", "202409", "202410", "202411", "202412", "202501", "202502", "202503", "202504", "202505"};

        try {
            // HTML 파일 작성 시작
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // HTML 헤더 작성
                writer.write("<!DOCTYPE html><html lang=\"ko\"><head><meta charset=\"UTF-8\"><title>EPL 전체 시즌 경기 일정</title>");
                writer.write("<style>");
                writer.write("table { width: 100%; border-collapse: collapse; }");
                writer.write("th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }");
                writer.write("th { background-color: #f2f2f2; }");
                writer.write("tr:nth-child(even) { background-color: #f9f9f9; }");
                writer.write("tr:hover { background-color: #f1f1f1; }");
                writer.write("img { width: 26px; height: 26px; vertical-align: middle; margin-right: 5px; }");
                writer.write("</style>");
                writer.write("</head><body>");
                writer.write("<h1>EPL 2024-2025 전체 시즌 경기 일정</h1><table><tr><th>날짜</th><th>경기 시간</th><th>경기장</th><th>홈팀</th><th>스코어</th><th>원정팀</th></tr>");

                // 각 월의 데이터를 순차적으로 크롤링
                for (String month : months) {
                    String baseUrl = "https://sports.daum.net/schedule/epl?date=" + month;
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
                    int rowspan = 0; // 같은 날짜에 대한 rowspan 값

                    for (int i = 0; i < matchElements.size(); i++) {
                        WebElement match = matchElements.get(i);

                        // 날짜 정보가 rowspan에 걸려있을 경우, 새롭게 날짜를 추출하고 rowspan 카운트를 시작
                        if (!match.findElements(By.cssSelector("td.td_date")).isEmpty()) {
                            // 다음 tr 태그들에 같은 날짜가 몇 개 있는지 계산
                            rowspan = 1; // 현재 경기를 포함한 갯수
                            for (int j = i + 1; j < matchElements.size(); j++) {
                                WebElement nextMatch = matchElements.get(j);
                                if (nextMatch.findElements(By.cssSelector("td.td_date")).isEmpty()) {
                                    rowspan++;
                                } else {
                                    break;
                                }
                            }
                            // 새로운 날짜 정보를 가져옴
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
                }

                // HTML 파일 마무리
                writer.write("</table></body></html>");
                System.out.println("EPL 전체 시즌 일정 크롤링 완료: " + filePath);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // WebDriver 종료
            driver.quit();
        }
    }
}
