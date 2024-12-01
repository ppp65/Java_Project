package com.example.project.repository;

import com.example.project.model.SportsPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SportsPredictionRepository extends JpaRepository<SportsPrediction, Long> {
    List<SportsPrediction> findByUserId(String userId);
    List<SportsPrediction> findByMatchDate(LocalDate matchDate);
    // 사용자, 경기 날짜, 시간, 경기장으로 예측 조회
    Optional<SportsPrediction> findByUserIdAndMatchDateAndMatchTimeAndStadium(
            String userId, LocalDate matchDate, String matchTime, String stadium);


    @Query("SELECT p FROM SportsPrediction p WHERE p.matchDate = :matchDate AND p.matchTime = :matchTime AND p.stadium = :stadium")
    List<SportsPrediction> findByMatchDateAndMatchTimeAndStadium(@Param("matchDate") LocalDate matchDate,
                                                                 @Param("matchTime") String matchTime,
                                                                 @Param("stadium") String stadium);

}