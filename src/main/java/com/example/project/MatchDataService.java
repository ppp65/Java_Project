package com.example.project;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatchDataService {

    private final JdbcTemplate jdbcTemplate;

    public MatchDataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String[]> getMatchesForMonth(int year, int month) {
        String sql = "SELECT MATCH_DATE, MATCH_TIME, STADIUM, HOMETEAM, SCORE, AWAYTEAM " +
                "FROM SPORTS_MATCH " +
                "WHERE EXTRACT(YEAR FROM MATCH_DATE) = ? AND EXTRACT(MONTH FROM MATCH_DATE) = ? " +
                "ORDER BY MATCH_DATE";

        return jdbcTemplate.query(sql,
                ps -> {
                    ps.setInt(1, year);
                    ps.setInt(2, month);
                },
                (rs, rowNum) -> new String[]{
                        rs.getDate("MATCH_DATE").toString(),
                        rs.getString("MATCH_TIME"),
                        rs.getString("STADIUM"),
                        rs.getString("HOMETEAM"),
                        rs.getString("SCORE"),
                        rs.getString("AWAYTEAM")
                }
        );
    }
}