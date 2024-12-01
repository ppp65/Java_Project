package com.example.project.service;

import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;  // 사용자 데이터베이스 접근

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 예측이 맞으면 사용자에게 포인트 지급
    public void awardPoints(String userId, int points) {
        // 포인트를 사용자에게 추가하는 로직
        userRepository.addPoints(userId, points);  // 사용자 포인트 추가 메서드
    }
}