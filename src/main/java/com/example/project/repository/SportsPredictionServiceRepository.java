package com.example.project.repository;

import com.example.project.model.SportsPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface SportsPredictionServiceRepository extends JpaRepository<SportsPrediction, Long> {

    List<SportsPrediction> findAll();

    @Query("SELECT p FROM SportsPrediction p WHERE p.matchDate = :matchDate AND p.matchTime = :matchTime AND p.stadium = :stadium")
    List<SportsPrediction> findPredictionsByMatch(@Param("matchDate") LocalDate matchDate,
                                                  @Param("matchTime") String matchTime,
                                                  @Param("stadium") String stadium);

    @Query("SELECT p FROM SportsPrediction p WHERE p.userId = :userId")
    List<SportsPrediction> findPredictionsByUser(@Param("userId") String userId);

    // evaluatePredictions는 Service 클래스에서 로직으로 구현되어야 함

    @Query("SELECT p FROM SportsPrediction p WHERE p.matchDate = :matchDate AND p.matchTime = :matchTime AND p.stadium = :stadium")
    List<SportsPrediction> findByMatchDateAndMatchTimeAndStadium(@Param("matchDate") LocalDate matchDate,
                                                                 @Param("matchTime") String matchTime,
                                                                 @Param("stadium") String stadium);
}