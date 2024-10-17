package com.example.project;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class EplDataInserter {

    // 파일 경로를 static 폴더로 변경
    private static final String FILE_PATH = "/Users/seungmin/IdeaProjects/Java_Project/src/main/resources/static/rank.html";

    public void insertDataToHtml(List<String[]> rankings) throws IOException {

        // 파일 내용을 읽음
        String htmlContent = new String(Files.readAllBytes(Paths.get(FILE_PATH)));

        StringBuilder tableContent = new StringBuilder();
        for (String[] row : rankings) {
            tableContent.append("<tr>");
            for (String cell : row) {
                tableContent.append("<td>").append(cell).append("</td>");
            }
            tableContent.append("</tr>");
        }

        // <tbody id="ranking-table"> 안에 데이터 삽입
        String updatedHtml = htmlContent.replaceAll("(?s)(<tbody id=\"ranking-table\">)(.*?)(</tbody>)", "$1" + tableContent.toString() + "$3");

        // 업데이트된 HTML을 파일로 저장
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(updatedHtml);
            System.out.println("HTML 파일이 성공적으로 업데이트되었습니다.");
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
}
