package com.example.project.service;

import com.example.project.model.User;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public void updateDailyPoints(User user) {
        LocalDate today = LocalDate.now();

        if (user.getLastLoginDate() == null || !user.getLastLoginDate().equals(today)) {
            user.setPoints(user.getPoints() + 10);
            user.setLastLoginDate(today);
            userRepository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        updateDailyPoints(user);

        return new CustomUserDetails(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getPoints(),
                user.getNicknameColor(),
                user.getTeamLogo(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }



    public void refreshSession(User updatedUser) {
        CustomUserDetails userDetails = new CustomUserDetails(
                updatedUser.getUserId(),
                updatedUser.getUsername(),
                updatedUser.getPassword(),
                updatedUser.getPoints(),
                updatedUser.getNicknameColor(),
                updatedUser.getTeamLogo(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public boolean userExists(String userId) {
        return userRepository.existsByUserId(userId);
    }

    public void saveUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            System.out.println("회원가입 성공: " + user.getUserId());
        } catch (Exception e) {
            System.err.println("회원 정보 저장 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("DB 저장 실패", e);
        }
    }
}
