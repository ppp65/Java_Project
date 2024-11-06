package com.example.project;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SkySportsEplCrawlerWithMore {

    public List<NewsDto> getNews() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        List<NewsDto> newsList = new ArrayList<>();

        try {
            String url = "https://www.skysports.com/premier-league-news";
            driver.get(url);

            List<WebElement> newsItems = driver.findElements(By.className("news-list__item"));

            for (WebElement item : newsItems) {
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

                newsList.add(new NewsDto(title, link, imageUrl, snippet, author, date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return newsList; // 크롤링한 뉴스 데이터 리스트 반환
    }

    private static String formatDate(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yy");
            Date date = inputFormat.parse(dateStr);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
            return outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }
    }
}
