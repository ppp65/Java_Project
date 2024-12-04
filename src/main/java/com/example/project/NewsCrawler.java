package com.example.project;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.*;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewsCrawler {

    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String DB_USERNAME = "system";
    private static final String DB_PASSWORD = "20193055";

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.6778.86 Safari/537.36");
        WebDriver driver = new ChromeDriver(options);

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Connected to Oracle DB successfully");

            driver.get("https://www.premierleague.com/news");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            JavascriptExecutor js = (JavascriptExecutor) driver;

            try {
                WebElement acceptCookiesButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.id("onetrust-accept-btn-handler")
                ));
                acceptCookiesButton.click();
                System.out.println("Clicked accept cookies button successfully");
            } catch (TimeoutException e) {
                System.out.println("The accept cookies button did not appear");
            }

            wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
            System.out.println("News main page loaded successfully");

            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(3000); // Wait for content to load

            List<WebElement> newsLinks = driver.findElements(By.cssSelector("a.media-thumbnail__link"));

            System.out.println("Number of news links found: " + newsLinks.size());

            Set<String> uniqueNewsUrls = new HashSet<>();
            for (WebElement link : newsLinks) {
                String relativeUrl = link.getAttribute("href");

                if (relativeUrl != null && relativeUrl.startsWith("//")) {
                    relativeUrl = "https:" + relativeUrl;
                }

                if (relativeUrl != null && !relativeUrl.isEmpty()) {
                    uniqueNewsUrls.add(relativeUrl);
                    System.out.println("Extracted link: " + relativeUrl);
                }
            }

            System.out.println("Number of unique news links extracted: " + uniqueNewsUrls.size());

            int counter = 0;
            for (String newsUrl : uniqueNewsUrls) {
                if (counter >= 25) {
                    break;
                }
                counter++;

                System.out.println("Crawling: " + newsUrl);
                driver.get(newsUrl);

                wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
                System.out.println("Page loaded successfully: " + newsUrl);

                try {
                    WebElement content = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//section[contains(@class, 'standardArticle')]")
                    ));
                    String articleHtml = content.getAttribute("outerHTML");

                    saveToDatabase(connection, newsUrl, articleHtml);

                } catch (Exception e) {
                    System.out.println("Failed to crawl content from: " + newsUrl);
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            System.err.println("An error occurred during the crawling process:");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Database connection closed");
                } catch (SQLException e) {
                    System.err.println("Error occurred while closing the database connection:");
                    e.printStackTrace();
                }
            }
            driver.quit();
            System.out.println("WebDriver closed successfully");
        }
    }

    private static void saveToDatabase(Connection connection, String url, String content) {
        String sql = "INSERT INTO SPORTS_NEWS (URL, CONTENT) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, url);
            pstmt.setString(2, content);

            int rowsInserted = pstmt.executeUpdate();
            System.out.println("Database INSERT successful - Rows inserted: " + rowsInserted);
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("URL already exists in the database, skipping: " + url);
        } catch (SQLException e) {
            System.err.println("Error occurred while saving to the database:");
            e.printStackTrace();
        }
    }
}
