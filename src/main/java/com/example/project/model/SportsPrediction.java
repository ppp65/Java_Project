package com.example.project.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "SPORTS_PREDICTION")
public class SportsPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prediction_seq")
    @SequenceGenerator(name = "prediction_seq", sequenceName = "PREDICTION_SEQ", allocationSize = 1)
    @Column(name = "PREDICTION_ID")
    private Long predictionId;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "MATCH_DATE", nullable = false)
    private LocalDate matchDate;

    @Column(name = "MATCH_TIME", nullable = false)
    private String matchTime;

    @Column(name = "STADIUM", nullable = false)
    private String stadium;

    @Column(name = "PREDICTED_WINNER", nullable = false)
    private String predictedWinner;

    @Column(name = "PREDICTION_TIMESTAMP", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime predictionTimestamp;

    @PrePersist
    public void prePersist() {
        // predictionTimestamp가 null일 경우에만 현재 시간을 설정
        if (this.predictionTimestamp == null) {
            this.predictionTimestamp = LocalDateTime.now();
        }
    }

    // 기본 생성자
    public SportsPrediction() {
    }

    // 사용자 예측 정보를 저장하는 생성자
    public SportsPrediction(String userId, String predictedWinner, LocalDate matchDate, String matchTime, String stadium) {
        this.userId = userId;
        this.predictedWinner = predictedWinner;
        this.matchDate = matchDate;
        this.matchTime = matchTime;
        this.stadium = stadium;
    }

    // Getter와 Setter 추가
    public Long getPredictionId() {
        return predictionId;
    }

    public void setPredictionId(Long predictionId) {
        this.predictionId = predictionId;
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

    public LocalDateTime getPredictionTimestamp() {
        return predictionTimestamp;
    }

    public void setPredictionTimestamp(LocalDateTime predictionTimestamp) {
        this.predictionTimestamp = predictionTimestamp;
    }
}