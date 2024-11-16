package com.example.project;

import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class OracleDBUploader {

    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "123123";

    /**
     * 뉴스 데이터를 DB에 업로드
     */
    public void News_uploadDataToOracleDB(List<NewsDto> crawledData) {
        String mergeSQL = "MERGE INTO SPORTS_NEWS sn " +
                "USING DUAL " +
                "ON (sn.TITLE = ?) " +
                "WHEN MATCHED THEN " +
                "UPDATE SET sn.NEWS_LINK = ?, sn.IMAGE_URL = ?, sn.SNIPPET = ?, sn.AUTHOR = ?, sn.NEWS_DATE = TO_DATE(?, 'YYYY-MM-DD') " +
                "WHEN NOT MATCHED THEN " +
                "INSERT (TITLE, NEWS_LINK, IMAGE_URL, SNIPPET, AUTHOR, NEWS_DATE) " +
                "VALUES (?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'))";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(mergeSQL)) {

            for (NewsDto news : crawledData) {
                try {
                    preparedStatement.setString(1, news.getTitle());
                    preparedStatement.setString(2, news.getLink());
                    preparedStatement.setString(3, news.getImageUrl());
                    preparedStatement.setString(4, news.getSnippet());
                    preparedStatement.setString(5, news.getAuthor());
                    preparedStatement.setString(6, news.getDate());
                    preparedStatement.setString(7, news.getTitle());
                    preparedStatement.setString(8, news.getLink());
                    preparedStatement.setString(9, news.getImageUrl());
                    preparedStatement.setString(10, news.getSnippet());
                    preparedStatement.setString(11, news.getAuthor());
                    preparedStatement.setString(12, news.getDate());

                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    System.err.println("DB에 뉴스 데이터를 삽입 중 오류 발생: " + e.getMessage());
                }
            }
            System.out.println("뉴스 데이터가 성공적으로 DB에 업로드되었습니다.");
        } catch (SQLException e) {
            Logger.getLogger(OracleDBUploader.class.getName()).log(Level.SEVERE, "DB 연결 오류", e);
        }
    }

    /**
     * 매치 데이터를 DB에 업로드
     */
    public void matchUploadDataToOracleDB(List<MatchDto> crawledData) {
        String mergeSQL = "MERGE INTO SPORTS_MATCH sm " +
                "USING DUAL " +
                "ON (sm.MATCH_DATE = TO_DATE(?, 'YYYY-MM-DD') AND sm.MATCH_TIME = ? AND sm.STADIUM = ?) " +
                "WHEN MATCHED THEN " +
                "UPDATE SET sm.SCORE = ?, sm.HOMETEAM = ?, sm.AWAYTEAM = ? " +
                "WHEN NOT MATCHED THEN " +
                "INSERT (MATCH_DATE, MATCH_TIME, STADIUM, HOMETEAM, SCORE, AWAYTEAM) " +
                "VALUES (TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(mergeSQL)) {

            for (MatchDto match : crawledData) {
                try {
                    String date = match.getDate();
                    String time = match.getTime(); // MATCH_TIME은 문자열로 처리
                    String stadium = match.getStadium();
                    String homeTeam = match.getHomeTeam();
                    String score = match.getScore();
                    String awayTeam = match.getAwayTeam();

                    // ON 조건의 매개변수 설정
                    preparedStatement.setString(1, date); // MATCH_DATE
                    preparedStatement.setString(2, time); // MATCH_TIME
                    preparedStatement.setString(3, stadium); // STADIUM

                    // UPDATE SET 매개변수 설정
                    preparedStatement.setString(4, score);
                    preparedStatement.setString(5, homeTeam);
                    preparedStatement.setString(6, awayTeam);

                    // INSERT 매개변수 설정
                    preparedStatement.setString(7, date); // MATCH_DATE
                    preparedStatement.setString(8, time); // MATCH_TIME
                    preparedStatement.setString(9, stadium); // STADIUM
                    preparedStatement.setString(10, homeTeam); // HOMETEAM
                    preparedStatement.setString(11, score); // SCORE
                    preparedStatement.setString(12, awayTeam); // AWAYTEAM

                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    System.err.println("DB에 매치 데이터를 삽입 중 오류 발생: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            System.out.println("매치 데이터가 성공적으로 DB에 업로드되었습니다.");
        } catch (SQLException e) {
            Logger.getLogger(OracleDBUploader.class.getName()).log(Level.SEVERE, "DB 연결 오류", e);
        }
    }
}
