package com.example.project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder; // URLEncoder 임포트 추가
import org.json.JSONArray;
import org.json.JSONObject;

public class EplHighlightsCrawler {
    private static final String API_KEY = "AIzaSyDmTi4f4WfW2HjpTi8RoN87wKZYOloHOUI"; // 유튜브 API 키
    private static final String CHANNEL_ID = "UCFK1eBq8Xy8E4p0h7I7YI8A"; // SPOTV 유튜브 채널 ID

    public static void main(String[] args) {
        try {
            String jsonResponse = getYouTubeVideos();
            String htmlOutput = generateHTML(jsonResponse);
            System.out.println(htmlOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getYouTubeVideos() throws Exception {
        // EPL 팀 이름 목록
        String[] teams = {
                "아스날 H/L", "A.빌라 H/L", "본머스 H/L",
                "브렌트포드 H/L", "브라이튼 H/L",
                "첼시 H/L", "C.팰리스 H/L", "에버턴 H/L",
                "풀럼 H/L", "입스위치 H/L",
                "맨시티 H/L", "맨유 H/L", "뉴캐슬 H/L",
                "노팅엄 H/L", "셰필드 H/L",
                "토트넘 H/L", "웨스트햄 H/L", "울버햄튼 H/L"
        };

        StringBuilder responseBuilder = new StringBuilder();

        for (String team : teams) {
            String urlString = "https://www.googleapis.com/youtube/v3/search?key=" + API_KEY
                    + "&channelId=" + CHANNEL_ID
                    + "&part=snippet,id&order=date&maxResults=5&q=" + URLEncoder.encode(team, "UTF-8"); // 인코딩 추가
            System.out.println("Request URL: " + urlString); // 요청 URL 출력

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder teamResponse = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                teamResponse.append(inputLine);
            }
            in.close();

            // API 응답을 출력
            System.out.println("API Response: " + teamResponse.toString());

            // 각 팀의 응답을 HTML로 변환하여 저장
            String htmlOutput = generateHTML(teamResponse.toString());
            responseBuilder.append(htmlOutput); // 모든 HTML을 하나로 합침
        }

        return responseBuilder.toString();
    }

    private static String generateHTML(String jsonResponse) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><body><h1>EPL 최근 H/L 영상</h1><ul>");

        // JSON 파싱
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray items = jsonObject.getJSONArray("items");

        boolean foundHighlight = false; // 하이라이트가 발견되었는지 여부

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String videoId = item.getJSONObject("id").getString("videoId");
            String title = item.getJSONObject("snippet").getString("title");
            String thumbnailUrl = item.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("default").getString("url");

            // 제목에 'SPOTV FOOTBALL', '24/25', 'UCL', 'PL'이 포함된 경우만 추가
            if (title.toLowerCase().contains("spotv football") &&
                    (title.toLowerCase().contains("24/25") ||
                            title.toLowerCase().contains("ucl") ||
                            title.toLowerCase().contains("pl"))) {
                html.append("<li>");
                html.append("<a href='https://www.youtube.com/watch?v=" + videoId + "'>" + title + "</a>");
                html.append("<br><img src='" + thumbnailUrl + "' alt='" + title + "' width='120' />");
                html.append("</li>");
                foundHighlight = true; // 하이라이트가 발견됨
            }
        }

        if (!foundHighlight) {
            // 하이라이트가 없을 경우 메시지 추가
            html.append("<li>No recent highlights found.</li>");
        }

        html.append("</ul></body></html>");

        saveHTMLToFile(html.toString(), "EPLHighlights.html");

        return html.toString();
    }

    private static void saveHTMLToFile(String htmlContent, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(htmlContent);
            System.out.println("HTML 파일이 저장되었습니다: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}