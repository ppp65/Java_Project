package com.example.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class SpotvEplHighlightsCrawler {
    public static void main(String[] args) {
        // ChromeDriver 설정
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 헤드리스 모드
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.6778.85 Safari/537.36");

        // ChromeDriver 인스턴스 생성
        WebDriver driver = new ChromeDriver(options);

        try {
            // SPOTV EPL 하이라이트 재생목록 URL
            driver.get("https://www.youtube.com/playlist?list=PL7MQjbfOyOE00FrDWwrbaTtH7mSZOKnvO");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ytd-playlist-video-renderer")));

            // 재생목록 동영상 가져오기
            List<WebElement> videos = driver.findElements(By.cssSelector("ytd-playlist-video-renderer"));

            // EPL 하이라이트 HTML 파일 생성
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("SpotvEplHighlights.html", false))) {
                writer.write("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>SPOTV EPL Highlights</title></head><body>");
                writer.write("<h1>SPOTV EPL Highlights</h1>");

                int videoCount = 0; // EPL 하이라이트 영상 카운트
                for (WebElement video : videos) {
                    if (videoCount >= 10) break; // 최대 5개만 가져오기

                    // 동영상 제목 가져오기
                    WebElement titleElement = video.findElement(By.cssSelector("#video-title"));
                    String title = titleElement.getText();

                    // 동영상 URL 추출
                    String videoUrl = titleElement.getAttribute("href");

                    // 썸네일 URL 가져오기
                    String thumbnailUrl = null;
                    try {
                        thumbnailUrl = video.findElement(By.cssSelector("img")).getAttribute("src");
                        if (thumbnailUrl == null || thumbnailUrl.isEmpty()) {
                            thumbnailUrl = video.findElement(By.cssSelector("img")).getAttribute("data-src");
                        }
                        if (thumbnailUrl == null || thumbnailUrl.isEmpty()) {
                            thumbnailUrl = "https://via.placeholder.com/528x308?text=No+Thumbnail";
                        }
                    } catch (Exception e) {
                        System.out.println("썸네일 URL 가져오기 실패: " + e.getMessage());
                    }

                    // 제목 필터링: EPL 하이라이트 조건 (예: "[24/25 PL]" 및 "H/L" 포함)
                    if (title.contains("PL") && title.contains("H/L")) {
                        // HTML 파일에 비디오 정보 작성
                        writer.write("<div>");
                        writer.write("<h2>" + title + "</h2>");
                        if (videoUrl != null) {
                            writer.write("<a href=\"" + videoUrl + "\"><img src=\"" + thumbnailUrl + "\" alt=\"" + title + "\" width=\"528\" height=\"308\"></a>");
                        } else {
                            writer.write("<p>동영상 URL을 가져올 수 없습니다.</p>");
                        }
                        writer.write("</div>");
                        videoCount++;
                    }
                }

                writer.write("</body></html>");
                System.out.println("파일 생성 완료: SpotvEplHighlights.html");
            } catch (IOException e) {
                System.out.println("파일 쓰기 중 오류 발생: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("크롤링 중 오류 발생: " + e.getMessage());
        } finally {
            driver.quit(); // 드라이버 종료
        }
    }
}
