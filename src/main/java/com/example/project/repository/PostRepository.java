package com.example.project.repository;

import com.example.project.model.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // JpaRepository에서 findById 메서드를 제공하므로 추가적인 메서드는 필요하지 않음
}