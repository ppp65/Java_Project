package com.example.project;
import okhttp3.*;
import org.json.JSONObject;

import java.sql.*;

public class TranslateAndSave {

    private static final String DEEPL_API_KEY = "본인 api키"; // DeepL API 키
    private static final String DEEPL_API_URL = "https://api-free.deepl.com/v2/translate";

    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String DB_USERNAME = "system";
    private static final String DB_PASSWORD = "20193055";

    public static void main(String[] args) {
        java.sql.Connection connection = null; 

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Connected to Oracle DB successfully");

            String selectSQL = "SELECT ID, URL, CONTENT FROM SPORTS_NEWS";
            PreparedStatement selectStmt = connection.prepareStatement(selectSQL);
            ResultSet resultSet = selectStmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String url = resultSet.getString("URL");
                String content = resultSet.getString("CONTENT");

                String translatedContent = translateText(content);

                saveTranslatedData(connection, id, url, translatedContent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Database connection closed");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String translateText(String text) throws Exception {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("auth_key", DEEPL_API_KEY)
                .add("text", text)
                .add("target_lang", "KO")
                .build();

        Request request = new Request.Builder()
                .url(DEEPL_API_URL)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new Exception("DeepL API error: " + response.message());
        }

        String responseBody = response.body().string();
        JSONObject jsonResponse = new JSONObject(responseBody);
        return jsonResponse.getJSONArray("translations").getJSONObject(0).getString("text");
    }

    private static void saveTranslatedData(java.sql.Connection connection, int id, String url, String translatedContent) {
        String insertSQL = "INSERT INTO SPORTS_TRANSLATE (ID, URL, CONTENT_TRANSLATED) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, url);
            pstmt.setString(3, translatedContent);

            int rowsInserted = pstmt.executeUpdate();
            System.out.println("Data inserted successfully: ID=" + id + ", URL=" + url);
        } catch (SQLException e) {
            System.err.println("Error inserting data: ID=" + id + ", URL=" + url);
            e.printStackTrace();
        }
    }
}