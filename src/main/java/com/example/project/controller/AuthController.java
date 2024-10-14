package com.example.project.controller;

import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestParam String nickname,
                                         @RequestParam String username,
                                         @RequestParam String password) {
        try {
            if (userService.isUsernameTaken(username)) {
                return ResponseEntity.badRequest().body("아이디가 이미 존재합니다.");
            }

            userService.registerUser(nickname, username, password);
            return ResponseEntity.ok("회원가입 성공");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }
}
