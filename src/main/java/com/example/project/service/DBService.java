package com.example.project.service;

import com.example.project.MatchDto;
import com.example.project.NewsDto;
import org.springframework.stereotype.Service;
import com.example.project.OracleDBUploader;

import java.util.List;

@Service
public class DBService {
    public void uploadNewsDataToOracle(List<NewsDto> newsData) {
        OracleDBUploader.News_uploadDataToOracleDB(newsData);
    }

    public void uploadMatchDataToOracle(List<MatchDto> matchData) {
        OracleDBUploader.Match_uploadDataToOracleDB(matchData);
    }
}