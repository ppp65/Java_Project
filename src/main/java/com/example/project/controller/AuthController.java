package com.example.project.controller;

import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        try {
            userService.signup(signupRequest.getUserId(), signupRequest.getUsername(), signupRequest.getPassword());
            return ResponseEntity.ok("Signup successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 현재 로그인한 사용자 정보를 반환하는 API
    @GetMapping("/current-user")
    public ResponseEntity<String> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("세션이 없거나 인증되지 않은 상태입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String username = authentication.getName();  // 로그인된 사용자 이름을 가져옴
        System.out.println("인증된 사용자: " + username);
        return ResponseEntity.ok(username);
    }

}

// 회원가입 요청 DTO
class SignupRequest {
    private String userId;   // 아이디
    private String username; // 닉네임
    private String password; // 비밀번호

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
