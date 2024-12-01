package com.example.project.service;

import com.example.project.DTO.SportsPredictionDTO;
import com.example.project.model.SportsMatchResult;
import com.example.project.model.SportsPrediction;
import com.example.project.model.User;
import com.example.project.repository.SportsMatchResultRepository;
import com.example.project.repository.SportsPredictionRepository;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class SportsPredictionService {

    private static final Logger logger = Logger.getLogger(SportsPredictionService.class.getName());

    @Autowired
    private final SportsPredictionRepository predictionRepository;
    private final SportsMatchResultRepository matchResultRepository;
    private final UserRepository userRepository;

    public SportsPredictionService(SportsPredictionRepository predictionRepository,
                                   SportsMatchResultRepository matchResultRepository,
                                   UserRepository userRepository) {
        this.predictionRepository = predictionRepository;
        this.matchResultRepository = matchResultRepository;
        this.userRepository = userRepository;
    }




    // 중복 예측 여부 확인 메서드
    public boolean checkIfUserHasPredicted(String userId, LocalDate matchDate, String matchTime, String stadium) {
        // 이미 예측이 존재하는지 확인
        Optional<SportsPrediction> existingPrediction = predictionRepository.findByUserIdAndMatchDateAndMatchTimeAndStadium(userId, matchDate, matchTime, stadium);
        return existingPrediction.isPresent();  // 이미 예측이 있으면 true, 없으면 false
    }


    public SportsPrediction savePrediction(SportsPrediction prediction) {
        if (prediction.getPredictionTimestamp() == null) {
            prediction.setPredictionTimestamp(LocalDateTime.now());
        }
        return predictionRepository.save(prediction);
    }

    public List<SportsPrediction> getPredictionsByUser(String userId) {
        return predictionRepository.findByUserId(userId);
    }

    public List<SportsPrediction> getPredictionsByMatch(LocalDate matchDate, String matchTime, String stadium) {
        return predictionRepository.findByMatchDateAndMatchTimeAndStadium(matchDate, matchTime, stadium);
    }

    // 예측을 저장하기 전에 중복 예측을 확인하는 메서드
    public void submitPrediction(SportsPrediction prediction) {
        // 중복 예측 체크: 같은 경기에서 같은 사용자가 예측한 경우
        Optional<SportsPrediction> existingPrediction = predictionRepository
                .findByUserIdAndMatchDateAndMatchTimeAndStadium(
                        prediction.getUserId(),
                        prediction.getMatchDate(),
                        prediction.getMatchTime(),
                        prediction.getStadium()
                );

        if (existingPrediction.isPresent()) {
            throw new IllegalStateException("You have already submitted a prediction for this match.");
        }

        // 예측이 없으면 저장
        predictionRepository.save(prediction);
    }

    // 경기 결과를 기반으로 예측을 평가하고 포인트를 지급하는 메서드
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
                } else {
                    // 예측이 틀리면, 부분 포인트를 줄 수도 있음
                    givePointsToUser(prediction.getUserId(), 5);  // 예시로 틀린 경우 5점 지급
                }
            }
        } else {
            logger.warning("No match result found for the given match parameters.");
        }
    }

    // 포인트 지급 메서드
    private void givePointsToUser(String userId, int points) {
        // 사용자 조회
        Optional<User> userOpt = userRepository.findByUserId(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            int currentPoints = user.getPoints();
            user.setPoints(currentPoints + points); // 현재 포인트에 추가

            // 포인트 업데이트
            userRepository.save(user);
            logger.info("Points updated for user: " + userId + " (+" + points + " points)");
        } else {
            // 사용자 없으면 예외 처리
            logger.severe("User not found: " + userId);
            throw new IllegalArgumentException("User not found: " + userId);
        }
    }
    public SportsPrediction savePredictionFromDTO(SportsPredictionDTO predictionDTO) {
        // 예측 DTO에서 엔티티로 변환
        SportsPrediction prediction = new SportsPrediction();
        prediction.setUserId(predictionDTO.getUserId());
        prediction.setPredictedWinner(predictionDTO.getPredictedWinner());
        prediction.setMatchDate(predictionDTO.getMatchDate());
        prediction.setMatchTime(predictionDTO.getMatchTime());
        prediction.setStadium(predictionDTO.getStadium());

        // 예측 저장 전에 중복 예측 여부 체크
        if (checkIfUserHasPredicted(predictionDTO.getUserId(), predictionDTO.getMatchDate(),
                predictionDTO.getMatchTime(), predictionDTO.getStadium())) {
            throw new IllegalStateException("You have already submitted a prediction for this match.");
        }

        // 예측 저장
        return predictionRepository.save(prediction);
    }
}