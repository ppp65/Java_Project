package com.example.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class SkySportsEplCrawlerWithMore {

    @Autowired
    private OracleDBUploader oracleDBUploader;  // OracleDBUploader 주입

    public List<NewsDto> executeCrawling() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);

        List<NewsDto> newsList = new ArrayList<>();
        StringBuilder newsContent = new StringBuilder();
        String filePath = Paths.get("src/main/resources/static/news.html").toAbsolutePath().toString();

        try {
            String url = "https://www.skysports.com/premier-league-news";
            driver.get(url);

            List<WebElement> newsItems = driver.findElements(By.className("news-list__item"));

            for (WebElement item : newsItems) {
                try {
                    WebElement titleElement = item.findElement(By.className("news-list__headline-link"));
                    WebElement imageElement = item.findElement(By.tagName("img"));
                    WebElement snippetElement = item.findElement(By.className("news-list__snippet"));
                    WebElement authorElement = item.findElement(By.className("label__tag"));
                    WebElement dateElement = item.findElement(By.className("label__timestamp"));

                    String title = titleElement.getText();
                    String link = titleElement.getAttribute("href");
                    String imageUrl = imageElement.getAttribute("data-src");
                    String snippet = snippetElement.getText();
                    String author = authorElement.getText();
                    String date = formatDate(dateElement.getText());

                    NewsDto news = new NewsDto(title, link, imageUrl, snippet, author, date);
                    newsList.add(news);

                    newsContent.append("<li><div style='display: flex; align-items: center; margin-bottom: 20px;'>");
                    newsContent.append("<a href='" + link + "' target='_blank'><img src='" + imageUrl + "' alt='News Image' style='width:300px; height:auto; margin-right: 20px;'></a>");
                    newsContent.append("<div><h2><a href='" + link + "' target='_blank'>" + title + "</a></h2>");
                    newsContent.append("<p><strong>Author:</strong> " + author + "</p>");
                    newsContent.append("<p><strong>Published:</strong> " + date + "</p>");
                    newsContent.append("<p>" + snippet + "</p></div></div></li>");
                } catch (NoSuchElementException e) {
                    System.err.println("뉴스 항목 처리 중 오류 발생: " + e.getMessage());
                }
            }

            oracleDBUploader.News_uploadDataToOracleDB(newsList);  // DB 업로드

            updateHtmlFile(filePath, newsContent.toString());
            System.out.println("크롤링 완료! news.html 파일이 성공적으로 업데이트되었습니다.");

        } catch (Exception e) {
            System.err.println("크롤링 작업 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return newsList;
    }

    private static String formatDate(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = inputFormat.parse(dateStr);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            return outputFormat.format(date);
        } catch (ParseException e) {
            System.err.println("날짜 형식 변환 실패: " + dateStr);
            return dateStr;
        }
    }

    private static void updateHtmlFile(String filePath, String newsContent) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            boolean insideNewsList = false;

            for (String line : lines) {
                if (line.contains("<ul id=\"news-list\">")) {
                    writer.write(line);
                    writer.newLine();
                    writer.write(newsContent);
                    writer.newLine();
                    insideNewsList = true;
                } else if (line.contains("</ul>") && insideNewsList) {
                    insideNewsList = false;
                    writer.write(line);
                    writer.newLine();
                } else if (!insideNewsList) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }
}
