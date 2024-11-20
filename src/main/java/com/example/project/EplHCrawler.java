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

public class EplHCrawler {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get("https://m.sports.naver.com/wfootball/video?category=epl&sort=date&tab=latest&themeType=type&themeCode=2");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".VideoList_video_list__3-MjZ")));

            // 스크롤을 통해 추가 항목 로드
            for (int i = 0; i < 3; i++) { // 3번 스크롤
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
                Thread.sleep(2000); // 2초 대기하여 추가 항목 로드
            }

            List<WebElement> videoItems = driver.findElements(By.cssSelector(".VideoList_video_list__3-MjZ"));
            System.out.println("비디오 항목 수: " + videoItems.size()); // 비디오 항목 수 확인
            if (videoItems.isEmpty()) {
                System.out.println("비디오 항목을 찾을 수 없습니다.");
                return; // 비디오 항목이 없으면 종료
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("EplHighlights.html", false))) {
                writer.write("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>EPL Highlights</title></head><body>");

                // 최대 5개 비디오 항목 처리
                int maxVideos = Math.min(videoItems.size(), 5);
                for (int i = 0; i < maxVideos; i++) {
                    WebElement item = videoItems.get(i);
                    String title = "";
                    String videoUrl = "";
                    String imageUrl = "";
                    String playTime = "";

                    try {
                        title = item.findElement(By.cssSelector(".VideoList_title__1EueY")).getText();
                        videoUrl = item.findElement(By.cssSelector("a")).getAttribute("href");
                        // 썸네일 이미지 URL 가져오기
                        imageUrl = item.findElement(By.cssSelector(".lazyload-wrapper img")).getAttribute("src");
                        playTime = item.findElement(By.cssSelector(".VideoList_common_playtime__1pZtJ")).getText();
                    } catch (NoSuchElementException e) {
                        System.out.println("요소를 찾을 수 없습니다: " + e.getMessage());
                        continue; // 요소를 찾지 못하면 다음 항목으로 넘어감
                    }

                    // 비디오 URL이 비어있거나 "null"인 경우 링크 클릭하여 URL 가져오기
                    if (videoUrl == null || videoUrl.isEmpty()) {
                        WebElement link = item.findElement(By.cssSelector("a"));
                        try {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
                            wait.until(ExpectedConditions.urlContains("video")); // URL이 "video"를 포함할 때까지 대기
                            videoUrl = driver.getCurrentUrl();
                        } catch (Exception e) {
                            System.out.println("링크 클릭 중 오류 발생: " + e.getMessage());
                            continue; // 클릭 중 오류가 발생하면 다음 항목으로 넘어감
                        } finally {
                            driver.navigate().back();
                            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".VideoList_video_list__3-MjZ"))); // 다시 요소를 찾기 위해 대기
                        }
                    }

                    // HTML 파일에 비디오 정보 작성
                    writer.write("<div>");
                    writer.write("<h2>" + title + "</h2>");
                    writer.write("<a href=\"" + videoUrl + "\"><img src=\"" + imageUrl + "\" alt=\"" + title + "\" width=\"528\" height=\"308\"></a>");
                    writer.write("<p>" + playTime + "</p>");
                    writer.write("</div>");
                }
                writer.write("</body></html>");
            } catch (IOException e) {
                System.out.println("파일 쓰기 중 오류 발생: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("크롤링 중 오류 발생: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}