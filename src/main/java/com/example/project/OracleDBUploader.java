package com.example.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OracleDBUploader {

    //DB 정보
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE";  // DB URL
    private static final String DB_USER = "system";  // DB Username
    private static final String DB_PASSWORD = "123123";  // DB Password

    public static void News_uploadDataToOracleDB(List<NewsDto> crawledData) {
            // 테이블과 데이터
            String insertSQL = "INSERT INTO SPORTS_NEWS (TITLE, NEWS_LINK, IMAGE_URL, SNIPPET, AUTHOR, NEWS_DATE) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

                // 크롤링된 데이터를 news에 삽입하는 것을 반복
                for (NewsDto news : crawledData) {
                    preparedStatement.setString(1, news.getTitle());  // Title
                    preparedStatement.setString(2, news.getLink());  // Link
                    preparedStatement.setString(3, news.getImageUrl());  // ImageUrl
                    preparedStatement.setString(4, news.getSnippet());  // Snippet
                    preparedStatement.setString(5, news.getAuthor());  // Author
                    preparedStatement.setDate(6, java.sql.Date.valueOf(news.getDate()));  // Date

                    // 데이터 삽입
                    preparedStatement.executeUpdate();
                }

                System.out.println("데이터가 성공적으로 DB에 업로드됨");

            } catch (SQLException e) {
                Logger.getLogger(OracleDBUploader.class.getName()).log(Level.SEVERE, "DB에 데이터를 업로드하는 것에 문제가 발생함", e);
            }
    }

    public static void Match_uploadDataToOracleDB(List<MatchDto> crawledData) {
        // 테이블과 데이터
        String insertSQL = "INSERT INTO SPORTS_MATCH (MATCH_DATE, MATCH_TIME, STADIUM, HOMETEAM, HOMETEAM_ICON, SCORE, AWAYTEAM, AWAYTEAM_ICON) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            // 크롤링된 데이터를 match에 삽입하는 것을 반복
            for (MatchDto match : crawledData) {
                preparedStatement.setDate(1, java.sql.Date.valueOf(match.getDate()));  // Match Date
                preparedStatement.setDate(2, java.sql.Date.valueOf(match.getTime()));  // Match Time
                preparedStatement.setString(3, match.getStadium());  // Stadium
                preparedStatement.setString(4, match.getHomeTeam());  // Home Team
                preparedStatement.setString(5, match.getHomeTeamIcon());  // Home Team Icon
                preparedStatement.setString(6, match.getScore());  // Score
                preparedStatement.setString(7, match.getAwayTeam());  // Away Team
                preparedStatement.setString(8, match.getAwayTeamIcon());  // Away Team Icon

                // 데이터 삽입
                preparedStatement.executeUpdate();
            }

            System.out.println("데이터가 성공적으로 DB에 업로드됨");

        } catch (SQLException e) {
            Logger.getLogger(OracleDBUploader.class.getName()).log(Level.SEVERE, "DB에 데이터를 업로드하는 것에 문제가 발생함", e);
        }
    }
}