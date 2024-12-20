package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.model.SportsMainLogo;
import com.example.project.repository.UserRepository;
import com.example.project.repository.MainLogoRepository;
import com.example.project.service.CustomUserDetails;
import com.example.project.service.CustomUserDetailsService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MainLogoRepository mainLogoRepository;

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
            response.put("nicknameColor", userDetails.getNicknameColor());
            response.put("teamLogo", userDetails.getTeamLogo());
            return response;
        } else {
            session.invalidate();
            response.put("message", "User is not authenticated");
            return response;
        }
    }

    @PostMapping("/update-nickname-color-and-points")
    public ResponseEntity<Map<String, String>> updateNicknameColorAndPoints(@RequestBody Map<String, Object> payload) {
        Map<String, String> response = new HashMap<>();
        String userId = (String) payload.get("userId");
        String color = (String) payload.get("color");
        int points = (int) payload.get("points");

        User user = userRepository.findByUserId(userId).orElse(null);

        if (user != null) {
            user.setNicknameColor(color);
            user.setPoints(points);
            userRepository.save(user);


            userDetailsService.refreshSession(user);

            response.put("message", "닉네임 색상이 업데이트되었습니다.");
            response.put("color", color);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "사용자를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        try {
            if (userDetailsService.userExists(user.getUserId())) {
                response.put("message", "아이디가 이미 존재합니다.");
                return ResponseEntity.badRequest().body(response);
            }
            userDetailsService.saveUser(user);
            response.put("message", "회원가입 성공");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "회원가입 처리 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/update-team-logo")
    public ResponseEntity<Map<String, String>> updateTeamLogo(@RequestBody Map<String, Object> payload) {
        Map<String, String> response = new HashMap<>();
        String userId = (String) payload.get("userId");
        String logoFileName = (String) payload.get("logo");
        int points = (int) payload.get("points");

        User user = userRepository.findByUserId(userId).orElse(null);

        if (user != null && user.getPoints() >= 300) {
            user.setTeamLogo(logoFileName);
            user.setPoints(points);
            userRepository.save(user);
            userDetailsService.refreshSession(user);

            response.put("message", "로고가 업데이트되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "사용자를 찾을 수 없거나 포인트가 부족합니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/update-main-logo")
    public ResponseEntity<Map<String, String>> updateMainLogo(@RequestBody Map<String, Object> payload) {
        Map<String, String> response = new HashMap<>();
        String userId = (String) payload.get("userId");
        String newLogo = (String) payload.get("logo");
        int points = (int) payload.get("points");

        User user = userRepository.findByUserId(userId).orElse(null);
        if (user == null) {
            response.put("message", "사용자를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (user.getPoints() < 500) {
            response.put("message", "포인트가 부족합니다.");
            return ResponseEntity.badRequest().body(response);
        }

        SportsMainLogo mainLogo = mainLogoRepository.findById(1L).orElse(null);
        LocalDate today = LocalDate.now();
        if (mainLogo != null && today.equals(mainLogo.getLastChanged())) {
            response.put("message", "오늘 이미 로고가 변경되었습니다.");
            return ResponseEntity.badRequest().body(response);
        }

        if (mainLogo == null) {
            mainLogo = new SportsMainLogo();
            mainLogo.setId(1L);
        }
        mainLogo.setLogoFileName(newLogo);
        mainLogo.setLastChanged(today);
        mainLogoRepository.save(mainLogo);

        user.setPoints(user.getPoints() - 500);
        userRepository.save(user);

        response.put("message", "로고가 성공적으로 변경되었습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-main-logo")
    @PermitAll
    public ResponseEntity<Map<String, String>> getMainLogo() {
        Map<String, String> response = new HashMap<>();
        try {
            SportsMainLogo mainLogo = mainLogoRepository.findById(1L).orElse(null);

            if (mainLogo != null) {
                response.put("logoFileName", mainLogo.getLogoFileName());
                return ResponseEntity.ok(response);
            } else {
                response.put("logoFileName", "");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("error", "Error fetching main logo");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }





}