package com.example.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "SPORTS_MATCH")
public class SportsMatch {

    @EmbeddedId
    private SportsMatchId id; // 복합 기본 키

    @Column(name = "HOMETEAM")
    private String homeTeam;

    @Column(name = "SCORE")
    private String score;

    @Column(name = "AWAYTEAM")
    private String awayTeam;

    // 기본 생성자
    public SportsMatch() {
        if (this.id == null) {
            this.id = new SportsMatchId(); // 기본값 생성
        }
    }

    public SportsMatch(SportsMatchId id, String homeTeam, String score, String awayTeam) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.score = score;
        this.awayTeam = awayTeam;
    }

    // Getter & Setter
    public SportsMatchId getId() {
        return id;
    }

    public void setId(SportsMatchId id) {
        this.id = id;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
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

    // 승자를 계산하는 메서드
    public String getWinner() {
        if (score == null || score.isEmpty()) {
            return "DRAW";
        }

        String[] scores = score.split(":");
        if (scores.length != 2) {
            return "DRAW";
        }

        int homeScore = Integer.parseInt(scores[0].trim());
        int awayScore = Integer.parseInt(scores[1].trim());

        if (homeScore > awayScore) {
            return "HOMETEAM";
        } else if (awayScore > homeScore) {
            return "AWAYTEAM";
        } else {
            return "DRAW";
        }
    }
}