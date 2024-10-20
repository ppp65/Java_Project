package com.example.project;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SkySportsEplCrawlerWithMore {
    public static void main(String[] args) {
        // WebDriver 설정
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // 백그라운드에서 실행
        WebDriver driver = new ChromeDriver(options);

        try {
            // 크롤링할 웹사이트 URL
            String url = "https://www.skysports.com/premier-league-news";
            driver.get(url);

            // 뉴스 리스트 아이템들 가져오기
            List<WebElement> newsItems = driver.findElements(By.className("news-list__item"));

            // HTML 파일 생성
            FileWriter fileWriter = new FileWriter("epl_news_with_image.html");
            fileWriter.write("<html><body><h1>Sky Sports EPL News</h1><ul>");

            for (WebElement item : newsItems) {
                // 뉴스 제목, 링크, 이미지, 요약 내용 추출
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
                String date = formatDate(dateElement.getText());  // 날짜 형식 변환

                // HTML 파일에 뉴스 추가 (이미지 및 제목에 링크 포함)
                fileWriter.write("<li>");
                fileWriter.write("<div style='display: flex; align-items: center;'>");

                // 이미지 크기를 키워서 링크 추가
                fileWriter.write("<a href='" + link + "'><img src='" + imageUrl + "' alt='News Image' style='width:400px; margin-right:20px'></a>");

                // 제목, 작성자, 작성일, 줄거리 등
                fileWriter.write("<div>");
                fileWriter.write("<h2><a href='" + link + "'>" + title + "</a></h2>");
                fileWriter.write("<p><strong>Author: </strong>" + author + "</p>");
                fileWriter.write("<p><strong>Published: </strong>" + date + "</p>");
                fileWriter.write("<p>" + snippet + "</p>");
                fileWriter.write("</div>");

                fileWriter.write("</div>");
                fileWriter.write("</li>");
            }

            fileWriter.write("</ul></body></html>");
            fileWriter.close();
            System.out.println("크롤링 완료! epl_news_with_image.html 파일이 생성되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // WebDriver 종료
            driver.quit();
        }
    }

    // 날짜 형식을 "xxxx/xx/xx"로 변환하는 함수
    private static String formatDate(String dateStr) {
        try {
            // 입력된 날짜가 'dd/MM/yy' 형식이라고 가정
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yy");
            Date date = inputFormat.parse(dateStr);

            // 출력 형식은 'yyyy/MM/dd'
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
            return outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;  // 날짜 형식 변환 실패 시 원본 문자열 반환
        }
    }
}
