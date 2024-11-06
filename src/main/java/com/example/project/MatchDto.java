package com.example.project;

public class MatchDto {
    private String date;
    private String time;
    private String stadium;
    private String homeTeam;
    private String homeTeamIcon;
    private String score;
    private String awayTeam;
    private String awayTeamIcon;

    // 생성자
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

    public String getHomeTeamIcon() {
        return homeTeamIcon;
    }

    public String getScore() {
        return score;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getAwayTeamIcon() {
        return awayTeamIcon;
    }

    // Setters
    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setHomeTeamIcon(String homeTeamIcon) {
        this.homeTeamIcon = homeTeamIcon;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setAwayTeamIcon(String awayTeamIcon) {
        this.awayTeamIcon = awayTeamIcon;
    }
}
