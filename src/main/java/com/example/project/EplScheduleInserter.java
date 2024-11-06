package com.example.project;

import org.springframework.stereotype.Component;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class EplScheduleInserter {

    public void insertScheduleToHtml(List<MatchDto> scheduleData) {
        try (FileWriter fileWriter = new FileWriter("src/main/resources/static/schedule.html")) {
            fileWriter.write("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n");
            fileWriter.write("<meta charset=\"UTF-8\" />\n<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\" />\n");
            fileWriter.write("<title>EPL Insight 일정</title>\n<link href=\"css/styles.css\" rel=\"stylesheet\" />\n</head>\n<body>\n");
            fileWriter.write("<h1 class=\"fw-bolder mb-1\">경기 일정</h1>\n");
            fileWriter.write("<table class=\"table table-bordered\">\n<thead>\n<tr>\n");
            fileWriter.write("<th>날짜</th><th>경기 시간</th><th>경기장</th><th>홈팀</th><th>스코어</th><th>원정팀</th>\n");
            fileWriter.write("</tr>\n</thead>\n<tbody>\n");

            final String[] currentRowDate = {""};
            for (MatchDto match : scheduleData) {
                fileWriter.write("<tr>\n");

                if (!currentRowDate[0].equals(match.getDate())) {
                    currentRowDate[0] = match.getDate();
                    int rowspanCount = (int) scheduleData.stream().filter(m -> m.getDate().equals(currentRowDate[0])).count();
                    fileWriter.write("<td rowspan=\"" + rowspanCount + "\">" + match.getDate() + "</td>\n");
                } else {
                    fileWriter.write("<td></td>\n");
                }

                fileWriter.write("<td>" + match.getTime() + "</td>\n");
                fileWriter.write("<td>" + match.getStadium() + "</td>\n");
                fileWriter.write("<td><img src=\"" + match.getHomeTeamIcon() + "\" alt=\"" + match.getHomeTeam() + "\" style=\"width:20px; height:20px; vertical-align:middle;\"> " + match.getHomeTeam() + "</td>\n");
                fileWriter.write("<td>" + match.getScore() + "</td>\n");
                fileWriter.write("<td><img src=\"" + match.getAwayTeamIcon() + "\" alt=\"" + match.getAwayTeam() + "\" style=\"width:20px; height:20px; vertical-align:middle;\"> " + match.getAwayTeam() + "</td>\n");

                fileWriter.write("</tr>\n");
            }

            fileWriter.write("</tbody>\n</table>\n</body>\n</html>");
            System.out.println("schedule.html 파일이 성공적으로 업데이트되었습니다.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
