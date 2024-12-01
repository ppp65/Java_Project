package com.example.project.repository;

import com.example.project.model.SportsMatch;
import com.example.project.model.SportsMatchId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SportsMatchRepository extends JpaRepository<SportsMatch, SportsMatchId> {
    List<SportsMatch> findByIdMatchDateAndIdMatchTimeAndIdStadium(LocalDate matchDate, String matchTime, String stadium);

    @Query("SELECT sm FROM SportsMatch sm WHERE sm.id.matchDate >= :currentDate")
    Page<SportsMatch> findUpcomingMatches(@Param("currentDate") LocalDate currentDate, Pageable pageable);
}