package com.example.project.repository;

import com.example.project.model.SportsMatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface SportsMatchResultRepository extends JpaRepository<SportsMatchResult, Long> {

    // 경기 날짜, 시간, 경기장으로 경기 결과를 조회하는 메서드
    @Query("SELECT m FROM SportsMatchResult m WHERE m.matchDate = :matchDate AND m.matchTime = :matchTime AND m.stadium = :stadium")
    Optional<SportsMatchResult> findByMatchDateAndMatchTimeAndStadium(@Param("matchDate") LocalDate matchDate,
                                                                      @Param("matchTime") String matchTime,
                                                                      @Param("stadium") String stadium);
}