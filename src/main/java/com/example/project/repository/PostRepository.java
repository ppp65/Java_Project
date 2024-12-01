package com.example.project.repository;

import com.example.project.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 게시글 작성 시간 이전 게시글
    Post findFirstByCreatedAtBeforeOrderByCreatedAtDesc(LocalDateTime createdAt);

    // 게시글 작성 시간 이후 게시글
    Post findFirstByCreatedAtAfterOrderByCreatedAtAsc(LocalDateTime createdAt);
}