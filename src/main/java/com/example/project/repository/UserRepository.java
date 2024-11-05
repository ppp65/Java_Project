package com.example.project.repository;

import com.example.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자 아이디로 사용자 찾기 (로그인 등을 위한 메서드)
    Optional<User> findByUserId(String userId);

    // 사용자 아이디 중복 여부 확인 (회원가입 시 사용)
    boolean existsByUserId(String userId);
}
