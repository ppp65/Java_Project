package com.example.project;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

@Component
public class EplScheduleInserter {

    private static final String FILE_PATH = "static/schedule.html"; // HTML 파일 경로

    public void insertScheduleToHtml(List<MatchDto> scheduleData) throws IOException {
        ClassPathResource resource = new ClassPathResource(FILE_PATH);
        File file = resource.getFile();
        String htmlContent = new String(Files.readAllBytes(file.toPath()));

        // 경기 일정 데이터를 HTML 표로 구성
        StringBuilder scheduleTable = new StringBuilder();
        for (MatchDto match : scheduleData) {
            scheduleTable.append("<tr>")
                    .append("<td>").append(match.getDate()).append("</td>")
                    .append("<td>").append(match.getTime()).append("</td>")
                    .append("<td>").append(match.getHomeTeam()).append("</td>")
                    .append("<td>").append(match.getScore()).append("</td>")
                    .append("<td>").append(match.getAwayTeam()).append("</td>")
                    .append("<td>").append(match.getStadium()).append("</td>")
                    .append("</tr>");
        }

        // 기존 HTML에서 <tbody id="schedule-table"> 부분을 찾아 데이터를 삽입
        String updatedHtml = htmlContent.replaceAll("(?s)(<tbody id=\"schedule-table\">)(.*?)(</tbody>)", "$1" + scheduleTable.toString() + "$3");

        // 업데이트된 HTML을 파일로 저장
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(updatedHtml);
            System.out.println("schedule.html 파일이 성공적으로 업데이트되었습니다.");
        }
    }
}
