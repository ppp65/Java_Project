package com.example.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Embeddable
public class SportsMatchId implements Serializable {

    @Column(name = "MATCH_DATE", nullable = false)
    @JsonProperty("matchDate")
    private LocalDate matchDate;

    @Column(name = "MATCH_TIME", nullable = false)
    @JsonProperty("matchTime")
    private String matchTime;

    @Column(name = "STADIUM", nullable = false)
    @JsonProperty("stadium")
    private String stadium;

    // 기본 생성자
    public SportsMatchId() {}

    public SportsMatchId(LocalDate matchDate, String matchTime, String stadium) {
        this.matchDate = matchDate;
        this.matchTime = matchTime;
        this.stadium = stadium;
    }

    // Getter와 Setter
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

    // equals()와 hashCode() 메서드 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SportsMatchId that = (SportsMatchId) o;
        return matchDate.equals(that.matchDate) &&
                matchTime.equals(that.matchTime) &&
                stadium.equals(that.stadium);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchDate, matchTime, stadium);
    }

    @Override
    public String toString() {
        return "SportsMatchId{" +
                "matchDate=" + matchDate +
                ", matchTime='" + matchTime + '\'' +
                ", stadium='" + stadium + '\'' +
                '}';
    }

}