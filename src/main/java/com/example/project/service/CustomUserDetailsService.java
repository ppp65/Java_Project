package com.example.project.service;

import com.example.project.model.User;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 매일 포인트 업데이트 메서드
    public void updateDailyPoints(User user) {
        LocalDate today = LocalDate.now();

        // 마지막 로그인 날짜가 오늘과 다른 경우에만 포인트 추가
        if (user.getLastLoginDate() == null || !user.getLastLoginDate().equals(today)) {
            user.setPoints(user.getPoints() + 10);  // 포인트 10점 추가
            user.setLastLoginDate(today);           // 마지막 로그인 날짜 갱신
            userRepository.save(user);              // 변경사항 저장
        }
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        // 매일 접속 포인트 업데이트
        updateDailyPoints(user);

        return new CustomUserDetails(
                user.getUserId(),
                user.getUsername(),  // 실제 username 필드 사용
                user.getPassword(),
                user.getPoints(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    public boolean userExists(String userId) {
        return userRepository.existsByUserId(userId);
    }

    public void saveUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);  // DB에 저장
            System.out.println("회원가입 성공: " + user.getUserId());
        } catch (Exception e) {
            System.err.println("회원 정보 저장 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("DB 저장 실패", e);  // 예외를 던져서 오류 응답을 반환하게 함
        }
    }
}
