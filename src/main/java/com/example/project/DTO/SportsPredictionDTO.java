package com.example.project.DTO;

import java.time.LocalDate;

public class SportsPredictionDTO {
    private String userId;
    private String predictedWinner;
    private LocalDate matchDate;
    private String matchTime;
    private String stadium;

    // 기본 생성자, Getter, Setter
    public SportsPredictionDTO() {}

    public SportsPredictionDTO(String userId, String predictedWinner, LocalDate matchDate, String matchTime, String stadium) {
        this.userId = userId;
        this.predictedWinner = predictedWinner;
        this.matchDate = matchDate;
        this.matchTime = matchTime;
        this.stadium = stadium;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPredictedWinner() {
        return predictedWinner;
    }

    public void setPredictedWinner(String predictedWinner) {
        this.predictedWinner = predictedWinner;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }
}