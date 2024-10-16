package com.example.project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class EplDataInserter {

    public static void insertDataToHtml(List<String[]> rankings) throws IOException {
        String filePath = "C:/Users/lordf/IdeaProjects/Java_Project/src/main/resources/templates/rank.html";
        String htmlContent = new String(Files.readAllBytes(Paths.get(filePath)));

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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(updatedHtml);
            System.out.println("HTML 파일이 성공적으로 업데이트되었습니다.");
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
}
