package com.example.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class SportsMatchResult {

    @Id
    private Long id;

    private LocalDate matchDate;
    private String matchTime;
    private String stadium;
    private int homeTeamScore;
    private int awayTeamScore;
    private String winner;  // HOMETEAM, AWAYTEAM, 또는 DRAW

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public LocalDate getMatchDate(){
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate){
        this.matchDate = matchDate;
    }

    public String getMatchTime(){
        return matchTime;
    }

    public void setMatchTime(String matchTime){
        this.matchTime = matchTime;
    }

    public String getStadium(){
        return stadium;
    }

    public void setStadium(String stadium){
        this.stadium = stadium;
    }

    public int getHomeTeamScore(){
        return homeTeamScore;
    }

    public void setHomeTeamScore(int homeTeamScore){
        this.homeTeamScore = homeTeamScore;
    }

    public int getAwayTeamScore(){
        return awayTeamScore;
    }

    public void setAwayTeamScore(int awayTeamScore){
        this.awayTeamScore = awayTeamScore;
    }

    public String getWinner(){
        return winner;
    }

    public void setWinner(String winner){
        this.winner = winner;
    }
}