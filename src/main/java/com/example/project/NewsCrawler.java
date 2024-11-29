package com.example.project;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

public class NewsCrawler {

    public static void main(String[] args) {
        // WebDriverManager를 사용해 크롬 드라이버 설정
        WebDriverManager.chromedriver().setup();

        // WebDriver 초기화
        WebDriver driver = new ChromeDriver();

        try {
            // 페이지 이동
            driver.get("https://www.premierleague.com/news/4176636");

            // 쿠키 허용 버튼 클릭
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            try {
                WebElement acceptCookiesButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.id("onetrust-accept-btn-handler")
                ));
                acceptCookiesButton.click();
                System.out.println("쿠키 허용 버튼 클릭 완료");
            } catch (TimeoutException e) {
                System.out.println("쿠키 허용 버튼이 나타나지 않았습니다.");
            }

            // 페이지 로드 대기 (JavaScript 확인)
            JavascriptExecutor js = (JavascriptExecutor) driver;
            wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
            System.out.println("페이지 로드 완료");

            // 본문 데이터 가져오기 (XPath로 수정)
            WebElement content = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//section[contains(@class, 'standardArticle')]")
            ));

            // HTML 추출
            String htmlContent = content.getAttribute("outerHTML");

            // HTML 파일로 저장
            saveHtmlToFile(htmlContent, "newnews.html");

            System.out.println("HTML 데이터가 newnews.html에 저장되었습니다.");
        } catch (Exception e) {
            System.err.println("크롤링 중 오류 발생:");
            e.printStackTrace();
        } finally {
            // 드라이버 종료
            driver.quit();
        }
    }

    private static void saveHtmlToFile(String htmlContent, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>크롤링 데이터</title>\n</head>\n<body>\n");
            writer.write(htmlContent);
            writer.write("\n</body>\n</html>");
        } catch (IOException e) {
            System.err.println("파일 저장 중 오류 발생:");
            e.printStackTrace();
        }
    }
}
