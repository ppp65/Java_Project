package com.example.project.repository;

import com.example.project.model.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAll();  // 모든 게시글을 반환하는 메서드
}