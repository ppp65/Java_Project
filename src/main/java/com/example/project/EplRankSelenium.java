package com.example.project;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class EplRankSelenium {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup(); // WebDriverManager를 사용하여 드라이버 설정

        WebDriver driver = new ChromeDriver();
        driver.get("https://sports.daum.net/record/epl");

        try {
            // 테이블 요소 찾기
            WebElement table = driver.findElement(By.className("tbl_record"));
            StringBuilder htmlOutput = new StringBuilder();
            htmlOutput.append("<html><body>");
            htmlOutput.append("<h1>EPL 순위표</h1>");
            htmlOutput.append(table.getAttribute("outerHTML"));
            htmlOutput.append("</body></html>");

            // HTML 파일로 저장
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("EPL_Rank.html"))) {
                writer.write(htmlOutput.toString());
                System.out.println("HTML 파일이 성공적으로 저장되었습니다.");
            } catch (IOException e) {
                System.out.println("파일 저장 중 오류가 발생했습니다.");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("테이블을 찾는 데 실패했습니다.");
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
