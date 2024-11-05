package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.service.CustomUserDetails;
import com.example.project.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class    AuthController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping("/check-auth")
    public Map<String, Object> checkAuth(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            response.put("message", "Authenticated");
            response.put("username", userDetails.getUsername());
            response.put("userId", userDetails.getUserId());
            response.put("points", userDetails.getPoints());
            return response;
        } else {
            session.invalidate();
            response.put("message", "User is not authenticated");
            return response;
        }
    }




    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        try {
            if (userDetailsService.userExists(user.getUserId())) {
                response.put("message", "아이디가 이미 존재합니다.");
                return ResponseEntity.badRequest().body(response);  // 400 Bad Request
            }
            userDetailsService.saveUser(user);
            response.put("message", "회원가입 성공");
            return ResponseEntity.ok(response);  // 200 OK
        } catch (Exception e) {
            response.put("message", "회원가입 처리 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);  // 500 Internal Server Error
        }
    }

}
