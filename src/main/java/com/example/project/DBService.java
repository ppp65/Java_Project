package com.example.project;

import com.example.project.MatchDto;
import com.example.project.NewsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBService {

    @Autowired
    private OracleDBUploader oracleDBUploader; // OracleDBUploader를 의존성 주입

    public void uploadNewsDataToOracle(List<NewsDto> newsData) {
        oracleDBUploader.News_uploadDataToOracleDB(newsData); // 인스턴스 메서드 호출
    }

    public void uploadMatchDataToOracle(List<MatchDto> matchData) {
        oracleDBUploader.matchUploadDataToOracleDB(matchData); // 인스턴스 메서드 호출
    }
}