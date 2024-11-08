package com.example.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OracleDBUploader {

    //DB 정보
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE";  // DB URL
    private static final String DB_USER = "system";  // DB Username
    private static final String DB_PASSWORD = "123123";  // DB Password

    public static void main(String[] args) {
        // 크롤링 데이터
        List<String[]> crawledData = List.of(
                new String[]{"John Doe", "john.doe@example.com", "Engineer"},
                new String[]{"Jane Smith", "jane.smith@example.com", "Manager"}
                //데이터 예시
        );

        // DB에 업데이트
        uploadDataToOracleDB(crawledData);
    }

    private static void uploadDataToOracleDB(List<String[]> crawledData) {
        String insertSQL = "INSERT INTO /*테이블 명*/ (/*필드*/) VALUES (/*데이터 값*/)";  // 테이블과 데이터 업데이트

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            // 크롤링된 데이터를 record에 삽입하는 것을 반복
            for (String[] record : crawledData) {
                preparedStatement.setString(1, record[0]);  // 0번째 열
                preparedStatement.setString(2, record[1]);  // 1번째 열
                preparedStatement.setString(3, record[2]);  // 2번째 열
                //추가 가능

                // 데이터 삽입
                preparedStatement.executeUpdate();
            }

            System.out.println("데이터가 성공적으로 DB에 업로드됨");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB에 데이터를 업로드하는 것에 문제가 발생함");
        }
    }
}