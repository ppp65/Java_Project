package com.example.project.DTO;

import com.example.project.model.SportsMatch;
import com.example.project.model.SportsMatchId;

import java.time.LocalDate;

public class SportsMatchDTO {
    private SportsMatchId sportsMatchId;  // Assuming it's a composite key or ID object
    private String homeTeam;
    private String awayTeam;
    private String score;

    // Constructor
    public SportsMatchDTO(SportsMatch match) {
        this.sportsMatchId = match.getId();  // Assuming `getId()` returns `SportsMatchId`
        this.homeTeam = match.getHomeTeam();
        this.awayTeam = match.getAwayTeam();
        this.score = match.getScore();
    }

    // Getters and Setters
    public SportsMatchId getSportsMatchId() {
        return sportsMatchId;
    }

    public void setSportsMatchId(SportsMatchId sportsMatchId) {
        this.sportsMatchId = sportsMatchId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}