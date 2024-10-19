package com.example.project.service;

import com.example.project.model.User;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User signup(String userId, String username, String password) {
        if (userRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException("User ID already exists");
        }

        User user = new User();
        user.setUserId(userId);  // 아이디 설정
        user.setUsername(username);  // 닉네임 설정
        user.setPassword(passwordEncoder.encode(password));  // 비밀번호 암호화 후 설정
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return userRepository.save(user);
    }
}
