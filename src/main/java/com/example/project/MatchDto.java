package com.example.project;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class MatchDto {
    private String date; // "YYYY-MM-DD" 형식
    private String time; // "HH:MM" 형식
    private String stadium;
    private String homeTeam;
    private String score;
    private String awayTeam;

    // 생성자
    public MatchDto(String date, String time, String stadium, String homeTeam, String score, String awayTeam) {
        this.date = validateAndFormatDate(date);
        this.time = validateAndFormatTime(time);
        this.stadium = stadium == null ? "알 수 없음" : stadium.trim();
        this.homeTeam = homeTeam == null ? "알 수 없음" : homeTeam.trim();
        this.score = score == null ? "경기 전" : score.trim();
        this.awayTeam = awayTeam == null ? "알 수 없음" : awayTeam.trim();
    }

    // Getters
    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStadium() {
        return stadium;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getScore() {
        return score;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    // Setters
    public void setDate(String date) {
        this.date = validateAndFormatDate(date);
    }

    public void setTime(String time) {
        this.time = validateAndFormatTime(time);
    }

    public void setStadium(String stadium) {
        this.stadium = stadium == null ? "알 수 없음" : stadium.trim();
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam == null ? "알 수 없음" : homeTeam.trim();
    }

    public void setScore(String score) {
        this.score = score == null ? "경기 전" : score.trim();
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam == null ? "알 수 없음" : awayTeam.trim();
    }

    // Helper Methods
    private String validateAndFormatDate(String date) {
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("날짜가 비어 있습니다.");
        }
        try {
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return parsedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            System.err.println("잘못된 날짜 형식: " + date + " (올바른 형식: yyyy-MM-dd)");
            throw new IllegalArgumentException("날짜 형식 오류: " + date, e);
        }
    }

    private String validateAndFormatTime(String time) {
        if (time == null || time.isEmpty()) {
            return "00:00"; // 기본값 설정
        }
        try {
            LocalTime parsedTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
            return parsedTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            System.err.println("잘못된 시간 형식: " + time + " (올바른 형식: HH:mm)");
            throw new IllegalArgumentException("시간 형식 오류: " + time, e);
        }
    }

    // equals 및 hashCode 구현 (중복 데이터 처리를 위해 필요)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchDto matchDto = (MatchDto) o;
        return Objects.equals(date, matchDto.date) &&
                Objects.equals(time, matchDto.time) &&
                Objects.equals(stadium, matchDto.stadium) &&
                Objects.equals(homeTeam, matchDto.homeTeam) &&
                Objects.equals(score, matchDto.score) &&
                Objects.equals(awayTeam, matchDto.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, stadium, homeTeam, score, awayTeam);
    }

    // toString() 메서드 추가 (디버깅 편의성 향상)
    @Override
    public String toString() {
        return "MatchDto{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", stadium='" + stadium + '\'' +
                ", homeTeam='" + homeTeam + '\'' +
                ", score='" + score + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                '}';
    }
}
