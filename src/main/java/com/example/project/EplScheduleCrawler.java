package com.example.project;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class EplScheduleCrawler {

    public static void main(String[] args) {
        // 크롤링할 URL 설정
        String url = "https://fbref.com/en/comps/9/schedule/Premier-League-Scores-and-Fixtures";

        // 출력할 HTML 파일 경로
        String filePath = Paths.get("EplSchedule.html").toAbsolutePath().toString();

        try {
            // 팀명 및 경기장명 한글 매핑 테이블
            Map<String, String> teamMap = new HashMap<>();
            Map<String, String> venueMap = new HashMap<>();

            // 한글화된 팀명 매핑
            teamMap.put("Liverpool", "리버풀");
            teamMap.put("Manchester City", "맨 시티");
            teamMap.put("Arsenal", "아스널");
            teamMap.put("Chelsea", "첼시");
            teamMap.put("Aston Villa", "애스턴 빌라");
            teamMap.put("Brighton", "브라이튼");
            teamMap.put("Newcastle United", "뉴캐슬");
            teamMap.put("Newcastle Utd", "뉴캐슬");
            teamMap.put("Fulham", "풀럼");
            teamMap.put("Tottenham", "토트넘");
            teamMap.put("Nottingham Forest", "노팅엄 포리스트");
            teamMap.put("Nott'ham Forest", "노팅엄 포리스트");
            teamMap.put("Brentford", "브렌트퍼드");
            teamMap.put("West Ham", "웨스트 햄");
            teamMap.put("Bournemouth", "본머스");
            teamMap.put("Manchester United", "맨유");
            teamMap.put("Manchester Utd", "맨유");
            teamMap.put("Leicester City", "레스터 시티");
            teamMap.put("Everton", "에버턴");
            teamMap.put("Ipswich Town", "입스위치");
            teamMap.put("Crystal Palace", "크리스탈 팰리스");
            teamMap.put("Southampton", "사우샘프턴");
            teamMap.put("Wolves", "울브스");

            // 한글화된 경기장 매핑
            venueMap.put("Old Trafford", "올드 트래포드");
            venueMap.put("Anfield", "안필드");
            venueMap.put("Emirates Stadium", "에미레이트 스타디움");
            venueMap.put("Stamford Bridge", "스탬포드 브리지");
            venueMap.put("Tottenham Hotspur Stadium", "토트넘 홋스퍼 스타디움");
            venueMap.put("Etihad Stadium", "에티하드 스타디움");
            venueMap.put("Villa Park", "빌라 파크");
            venueMap.put("Goodison Park", "구디슨 파크");
            venueMap.put("King Power Stadium", "킹 파워 스타디움");
            venueMap.put("The American Express Stadium", "아메리칸 익스프레스 스타디움");
            venueMap.put("Selhurst Park", "셀허스트 파크");
            venueMap.put("London Stadium", "런던 스타디움");
            venueMap.put("Craven Cottage", "크레이븐 코티지");
            venueMap.put("Molineux Stadium", "몰리뉴 스타디움");
            venueMap.put("Gtech Community Stadium", "지테크 커뮤니티 스타디움");
            venueMap.put("St Mary's Stadium", "세인트 메리즈 스타디움");
            venueMap.put("The City Ground", "더 시티 그라운드");
            venueMap.put("Vitality Stadium", "비탈리티 스타디움");
            venueMap.put("Portman Road Stadium", "포트만 로드 스타디움");
            venueMap.put("St James' Park", "세인트 제임스 파크");

            // Jsoup을 사용하여 페이지를 크롤링
            Document document = Jsoup.connect(url).get();

            // 경기 일정 테이블 찾기
            Element scheduleTable = document.select("table.stats_table").first();
            Elements rows = scheduleTable.select("tbody tr");

            // 시간 형식 파싱을 위한 포맷터
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            // HTML 파일 작성 시작
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // HTML 헤더 작성
                writer.write("<!DOCTYPE html><html lang=\"ko\"><head><meta charset=\"UTF-8\"><title>EPL 경기 일정</title>");
                writer.write("<style>");
                writer.write("table { width: 100%; border-collapse: collapse; }");
                writer.write("th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }");
                writer.write("th { background-color: #f2f2f2; }");
                writer.write("tr:nth-child(even) { background-color: #f9f9f9; }");
                writer.write("tr:hover { background-color: #f1f1f1; }");
                writer.write(".highlight { background-color: #cccccc; font-weight: bold; }");  // 진한 스타일 적용
                writer.write("</style>");
                writer.write("</head><body>");
                writer.write("<h1>EPL 경기 일정</h1><table><tr><th>날짜</th><th>시작 시각</th><th>홈팀</th><th>스코어</th><th>상대팀</th><th>경기장</th></tr>");

                String previousDate = ""; // 이전 날짜 저장

                // 경기 일정 데이터 파싱
                for (Element row : rows) {
                    // 날짜, 홈팀, 스코어, 상대팀, 장소 정보 추출
                    String date = row.select("td[data-stat='date']").text(); // 날짜
                    String homeTeam = row.select("td[data-stat='home_team']").text(); // 홈팀
                    String score = row.select("td[data-stat='score']").text(); // 스코어
                    String awayTeam = row.select("td[data-stat='away_team']").text(); // 상대팀
                    String venue = row.select("td[data-stat='venue']").text(); // 경기장

                    // 스코어가 비어있을 경우 "경기 전"으로 설정
                    if (score.isEmpty()) {
                        score = "경기 전";
                    }

                    // 날짜가 비어있을 경우, 이전 날짜 유지
                    if (date.isEmpty()) {
                        date = previousDate;
                    } else {
                        previousDate = date; // 새로운 날짜를 업데이트
                    }

                    // 시간 데이터 추출
                    Element localTimeElement = row.selectFirst("span.localtime");
                    Element venueTimeElement = row.selectFirst("span.venuetime");

                    // localtime이 없으면 venuetime 사용
                    String time = "시간 미정"; // 기본값을 한글로 설정
                    if (localTimeElement != null && !localTimeElement.text().isEmpty()) {
                        // 괄호 제거 후 시간 값만 가져오기
                        time = localTimeElement.text().replaceAll("[()]", "");
                    } else if (venueTimeElement != null && !venueTimeElement.attr("data-venue-time").isEmpty()) {
                        time = venueTimeElement.attr("data-venue-time"); // 경기 시간
                    }

                    // 8시간을 더하기 위한 처리 (시간 형식이 올바를 경우만 처리)
                    if (!time.equals("시간 미정")) {
                        try {
                            // 시간 파싱 및 8시간 더하기
                            LocalTime parsedTime = LocalTime.parse(time, timeFormatter);
                            LocalTime adjustedTime = parsedTime.plusHours(8);
                            // 시간을 다시 문자열로 변환
                            time = adjustedTime.format(timeFormatter);
                        } catch (Exception e) {
                            // 만약 시간 형식이 잘못되었으면 예외 처리 후 "시간 미정"으로 유지
                            time = "시간 미정";
                        }
                    }

                    // 팀명 및 경기장명 한글화
                    homeTeam = teamMap.getOrDefault(homeTeam, homeTeam); // 홈팀 한글 변환
                    awayTeam = teamMap.getOrDefault(awayTeam, awayTeam); // 상대팀 한글 변환
                    venue = venueMap.getOrDefault(venue, venue); // 경기장명 한글 변환

                    // 시간이 없는 경우 날짜와 시간을 지우고 스타일 적용
                    if (time.equals("시간 미정")) {
                        // 스코어가 "경기 전"인 경우, 스코어를 출력하지 않음
                        if (score.equals("경기 전")) {
                            writer.write("<tr class='highlight'><td colspan='2'></td><td>" + homeTeam + "</td><td></td><td>" + awayTeam + "</td><td>" + venue + "</td></tr>");
                        } else {
                            writer.write("<tr class='highlight'><td colspan='2'></td><td>" + homeTeam + "</td><td>" + score + "</td><td>" + awayTeam + "</td><td>" + venue + "</td></tr>");
                        }
                    } else {
                        // 시간이 있는 경우 정상 출력
                        writer.write("<tr><td>" + date + "</td><td>" + time + "</td><td>" + homeTeam + "</td><td>" + score + "</td><td>" + awayTeam + "</td><td>" + venue + "</td></tr>");
                    }

                }

                // HTML 파일 마무리
                writer.write("</table></body></html>");
                System.out.println("EPL 일정 크롤링 완료: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
