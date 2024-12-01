package com.example.project.controller;

import com.example.project.model.SportsMatchResult;
import com.example.project.repository.SportsMatchResultRepository;
import com.example.project.service.SportsPredictionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/match-results")
public class SportsMatchResultController {

    private final SportsMatchResultRepository matchResultRepository;
    private final SportsPredictionService predictionService;

    public SportsMatchResultController(SportsMatchResultRepository matchResultRepository,
                                       SportsPredictionService predictionService) {
        this.matchResultRepository = matchResultRepository;
        this.predictionService = predictionService;
    }

    // 경기 결과 저장
    @PostMapping
    public ResponseEntity<SportsMatchResult> saveMatchResult(@RequestBody SportsMatchResult matchResult) {
        SportsMatchResult savedResult = matchResultRepository.save(matchResult);

        // 결과 저장 후 예측 평가
        predictionService.evaluatePredictions(matchResult.getMatchDate(), matchResult.getMatchTime(), matchResult.getStadium());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedResult);
    }
}