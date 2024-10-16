package com.example.project;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EplRank_wj {
    public static void main(String[] args) {
        String url = "https://sports.daum.net/record/epl";
        try {
            Document document = Jsoup.connect(url).get();

            Element table = document.selectFirst(".tbl_record");

            if (table != null) {
                StringBuilder htmlOutput = new StringBuilder();
                htmlOutput.append("<html><body>");
                htmlOutput.append("<h1>EPL 순위표</h1>");
                htmlOutput.append(table.outerHtml());
                htmlOutput.append("</body></html>");

                // HTML 파일로 저장
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("EPL_Rank.html"))) {
                    writer.write(htmlOutput.toString());
                    System.out.println("HTML 파일이 성공적으로 저장되었습니다.");
                } catch (IOException e) {
                    System.out.println("파일 저장 중 오류가 발생했습니다.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("테이블을 찾을 수 없습니다.");
            }
        } catch (IOException e) {
            System.out.println("페이지를 가져오는 데 실패했습니다.");
            e.printStackTrace();
        }
    }
}