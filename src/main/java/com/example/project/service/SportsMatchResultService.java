package com.example.project.service;

import com.example.project.model.SportsMatchResult;
import com.example.project.repository.SportsMatchResultRepository;
import org.springframework.stereotype.Service;

@Service
public class SportsMatchResultService {

    private final SportsMatchResultRepository matchResultRepository;
    private final SportsPredictionService sportsPredictionService;

    public SportsMatchResultService(SportsMatchResultRepository matchResultRepository,
                                    SportsPredictionService sportsPredictionService) {
        this.matchResultRepository = matchResultRepository;
        this.sportsPredictionService = sportsPredictionService;
    }

    // 경기 결과를 업데이트하는 메서드
    public void updateMatchResult(SportsMatchResult matchResult) {
        // 경기 결과 업데이트
        matchResultRepository.save(matchResult);

        // 경기 결과가 업데이트된 후 예측을 평가하고 포인트를 지급
        sportsPredictionService.evaluatePredictions(
                matchResult.getMatchDate(),
                matchResult.getMatchTime(),
                matchResult.getStadium()
        );
    }
}