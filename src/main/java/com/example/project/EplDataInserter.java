package com.example.project;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

@Component
public class EplDataInserter {

    private static final String FILE_PATH = "static/rank.html";

    public void insertDataToHtml(List<String[]> rankings) throws IOException {
        ClassPathResource resource = new ClassPathResource(FILE_PATH);

        // 파일 내용을 읽음
        File file = resource.getFile();
        String htmlContent = new String(Files.readAllBytes(file.toPath()));

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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(updatedHtml);
            System.out.println("HTML 파일이 성공적으로 업데이트되었습니다.");
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
}
