package com.example.project;

public class MatchDto {
    private String date;
    private String time;
    private String stadium;
    private String homeTeam;
    private String homeTeamIcon;  // 홈팀 아이콘 URL
    private String score;
    private String awayTeam;
    private String awayTeamIcon;  // 원정팀 아이콘 URL

    public MatchDto(String date, String time, String stadium, String homeTeam, String homeTeamIcon, String score, String awayTeam, String awayTeamIcon) {
        this.date = date;
        this.time = time;
        this.stadium = stadium;
        this.homeTeam = homeTeam;
        this.homeTeamIcon = homeTeamIcon;
        this.score = score;
        this.awayTeam = awayTeam;
        this.awayTeamIcon = awayTeamIcon;
    }

    // Getters와 Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getHomeTeamIcon() {
        return homeTeamIcon;
    }

    public void setHomeTeamIcon(String homeTeamIcon) {
        this.homeTeamIcon = homeTeamIcon;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getAwayTeamIcon() {
        return awayTeamIcon;
    }

    public void setAwayTeamIcon(String awayTeamIcon) {
        this.awayTeamIcon = awayTeamIcon;
    }
}
