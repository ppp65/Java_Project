package com.example.project.repository;

import com.example.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    @Modifying
    @Query("UPDATE User u SET u.points = u.points + :points WHERE u.userId = :userId")
    int addPoints(@Param("userId") String userId, @Param("points") int points);

    boolean existsByUserId(String userId);
}