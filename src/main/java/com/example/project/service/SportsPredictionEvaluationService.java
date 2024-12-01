package com.example.project.service;

import com.example.project.model.SportsMatchResult;
import com.example.project.model.SportsPrediction;
import com.example.project.model.User;
import com.example.project.repository.SportsMatchResultRepository;
import com.example.project.repository.SportsPredictionRepository;
import com.example.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import java.util.Optional;

@Service
public class SportsPredictionEvaluationService {

    private final SportsPredictionRepository predictionRepository;
    private final SportsMatchResultRepository matchResultRepository;
    private final UserRepository userRepository;  // UserRepository 주입

    public SportsPredictionEvaluationService(SportsPredictionRepository predictionRepository,
                                             SportsMatchResultRepository matchResultRepository,
                                             UserRepository userRepository) {
        this.predictionRepository = predictionRepository;
        this.matchResultRepository = matchResultRepository;
        this.userRepository = userRepository;
    }

    public void evaluatePredictions(LocalDate matchDate, String matchTime, String stadium) {
        // 경기 결과를 조회
        Optional<SportsMatchResult> matchResultOpt = matchResultRepository
                .findByMatchDateAndMatchTimeAndStadium(matchDate, matchTime, stadium);

        if (matchResultOpt.isPresent()) {
            SportsMatchResult matchResult = matchResultOpt.get();
            String actualWinner = matchResult.getWinner(); // 실제 승자

            // 해당 경기 예측을 제출한 사용자들을 조회
            List<SportsPrediction> predictions = predictionRepository
                    .findByMatchDateAndMatchTimeAndStadium(matchDate, matchTime, stadium);

            // 예측이 맞는지 평가
            for (SportsPrediction prediction : predictions) {
                if (prediction.getPredictedWinner().equals(actualWinner)) {
                    // 예측이 맞으면 포인트 지급
                    givePointsToUser(prediction.getUserId(), 10);  // 예시로 10점 지급
                }
            }
        }
    }

    private void givePointsToUser(String userId, int points) {
        // 사용자 조회
        Optional<User> userOpt = userRepository.findByUserId(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            int currentPoints = user.getPoints();
            user.setPoints(currentPoints + points); // 현재 포인트에 추가

            // 포인트 업데이트
            userRepository.save(user);
        } else {
            // 사용자 없으면 예외 처리
            throw new IllegalArgumentException("User not found: " + userId);
        }
    }

    private void deductPointsFromUser(String userId, int points) {
        // 사용자 조회
        Optional<User> userOpt = userRepository.findByUserId(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            int currentPoints = user.getPoints();
            user.setPoints(currentPoints - points); // 현재 포인트에서 차감

            // 포인트 업데이트
            userRepository.save(user);
        } else {
            // 사용자 없으면 예외 처리
            throw new IllegalArgumentException("User not found: " + userId);
        }
    }
}