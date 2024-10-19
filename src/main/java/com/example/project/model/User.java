package com.example.project.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "SPORTS_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "SPORTS_USER_SEQ", allocationSize = 1)
    @Column(name = "ID") // 자동 증가하는 고유 ID
    private Long id;

    @Column(name = "USER_ID", nullable = false, unique = true) // 사용자가 입력한 아이디
    private String userId;

    @Column(name = "USERNAME", nullable = false) // 닉네임
    private String username;

    @Column(name = "PASSWORD", nullable = false) // 비밀번호
    private String password;

    @Column(name = "CREATED_AT", nullable = false) // 생성 시간
    private Timestamp createdAt;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
