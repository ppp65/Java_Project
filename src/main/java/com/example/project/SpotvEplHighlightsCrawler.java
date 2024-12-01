package com.example.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

@Component
public class SpotvEplHighlightsCrawler {

    public void executeCrawling() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("user-agent=Mozilla/5.0");

        WebDriver driver = new ChromeDriver(options);

        try {
            // YouTube 재생목록 URL로 이동
            driver.get("https://www.youtube.com/playlist?list=PL7MQjbfOyOE00FrDWwrbaTtH7mSZOKnvO");

            // WebDriverWait 설정 (Duration 사용)
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ytd-playlist-video-renderer")));

            // 비디오 데이터 수집
            List<WebElement> videos = driver.findElements(By.cssSelector("ytd-playlist-video-renderer"));
            StringBuilder highlightsContent = new StringBuilder();

            // 최대 10개의 비디오를 처리
            int videoCount = 0;
            for (WebElement video : videos) {
                if (videoCount >= 10) break;

                try {
                    // 비디오 제목과 URL 가져오기
                    WebElement titleElement = video.findElement(By.cssSelector("#video-title"));
                    String title = titleElement.getText();
                    String videoUrl = titleElement.getAttribute("href");

                    // 썸네일 URL 가져오기
                    String thumbnailUrl = getThumbnailUrl(video);

                    // HTML 코드 작성
                    highlightsContent.append("<li>")
                            .append("<div style='display: flex; align-items: center; margin-bottom: 20px;'>")
                            .append("<a href='").append(videoUrl).append("' target='_blank'>")
                            .append("<img src='").append(thumbnailUrl).append("' alt='Highlight Image' style='width:300px; height:auto; margin-right: 20px;'>")
                            .append("</a>")
                            .append("<div><h2><a href='").append(videoUrl).append("' target='_blank'>").append(title).append("</a></h2></div>")
                            .append("</div>")
                            .append("</li>");
                    videoCount++;
                } catch (Exception e) {
                    System.out.println("비디오 정보 처리 중 오류 발생: " + e.getMessage());
                }
            }

            // HTML 파일 업데이트
            updateHtmlFile("src/main/resources/static/highlights.html", highlightsContent.toString());

            System.out.println("하이라이트 데이터 삽입 완료!");
        } catch (Exception e) {
            System.out.println("크롤링 중 오류 발생: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }

    /**
     * 썸네일 URL 가져오기
     */
    private String getThumbnailUrl(WebElement video) {
        try {
            String thumbnailUrl = video.findElement(By.cssSelector("img")).getAttribute("src");
            if (thumbnailUrl == null || thumbnailUrl.isEmpty()) {
                thumbnailUrl = video.findElement(By.cssSelector("img")).getAttribute("data-src");
            }
            if (thumbnailUrl == null || thumbnailUrl.isEmpty()) {
                thumbnailUrl = "https://via.placeholder.com/528x308?text=No+Thumbnail";
            }
            return thumbnailUrl;
        } catch (Exception e) {
            System.out.println("썸네일 URL 가져오기 실패: " + e.getMessage());
            return "https://via.placeholder.com/528x308?text=No+Thumbnail";
        }
    }

    /**
     * HTML 파일 업데이트: <ul id="highlights-list"> 내부에 데이터 삽입
     */
    private void updateHtmlFile(String filePath, String highlightsContent) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            boolean insideHighlightsList = false;

            for (String line : lines) {
                // <ul id="highlights-list"> 태그를 찾으면 데이터 삽입
                if (line.contains("<ul id=\"highlights-list\">")) {
                    writer.write(line);
                    writer.newLine();
                    writer.write(highlightsContent);
                    writer.newLine();
                    insideHighlightsList = true;
                }
                // </ul> 태그를 만나면 삽입 종료
                else if (line.contains("</ul>") && insideHighlightsList) {
                    insideHighlightsList = false;
                    writer.write(line);
                    writer.newLine();
                }
                // 그 외에는 원본 파일 내용을 그대로 작성
                else if (!insideHighlightsList) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }
}
