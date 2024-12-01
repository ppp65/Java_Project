package com.example.project.controller;

import com.example.project.DTO.SportsPredictionDTO;
import com.example.project.model.SportsPrediction;
import com.example.project.service.SportsPredictionService;
import com.example.project.repository.SportsPredictionServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/predictions")
public class SportsPredictionController {

    @Autowired
    private SportsPredictionService predictionService;

    @Autowired
    private SportsPredictionServiceRepository predictionServiceRepository;

    // 특정 경기의 예측 데이터를 가져오는 API
    @GetMapping("/match")
    public List<SportsPrediction> getPredictionsByMatch(
            @RequestParam("matchDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate matchDate,
            @RequestParam("matchTime") String matchTime,
            @RequestParam("stadium") String stadium) {
        return predictionService.getPredictionsByMatch(matchDate, matchTime, stadium);
    }

    // 사용자의 예측 데이터를 가져오는 API
    @GetMapping("/user")
    public List<SportsPrediction> getPredictionsByUser(
            @RequestParam("userId") String userId) {
        return predictionService.getPredictionsByUser(userId);
    }

    // 모든 예측 가져오기 API
    @GetMapping("/all")
    public ResponseEntity<List<SportsPrediction>> getAllPredictions() {
        try {
            List<SportsPrediction> predictions = predictionServiceRepository.findAll();
            return new ResponseEntity<>(predictions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 예측 데이터를 저장하는 API (예측을 제출하기 전에 사용하는 API)
    @PostMapping("/save")
    public ResponseEntity<String> savePrediction(@RequestBody SportsPrediction prediction) {
        predictionService.savePrediction(prediction);
        return new ResponseEntity<>("Prediction saved successfully!", HttpStatus.OK);
    }

    @Autowired
    public SportsPredictionController(SportsPredictionService predictionService) {
        this.predictionService = predictionService;
    }

    // 예측 제출 API
    @PostMapping("/submit")
    public ResponseEntity<Map<String, String>> submitPrediction(@RequestBody SportsPredictionDTO predictionDTO) {
        try {
            predictionService.savePredictionFromDTO(predictionDTO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Prediction submitted successfully.");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 예측 상태 확인 API
    @GetMapping("/checkPredictionStatus")
    public ResponseEntity<Map<String, Boolean>> checkPredictionStatus(
            @RequestParam String stadium,
            @RequestParam String matchTime,
            @RequestParam LocalDate matchDate,
            @RequestParam String userId) {

        boolean predicted = predictionService.checkIfUserHasPredicted(userId, matchDate, matchTime, stadium);
        Map<String, Boolean> response = new HashMap<>();
        response.put("predicted", predicted);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 예측 평가 및 포인트 지급 처리
    @PostMapping("/evaluate")
    public String evaluatePrediction(
            @RequestParam LocalDate matchDate,
            @RequestParam String matchTime,
            @RequestParam String stadium) {
        // 예측을 평가하고 포인트 지급
        predictionService.evaluatePredictions(matchDate, matchTime, stadium);
        return "Predictions evaluated and points awarded!";
    }
}