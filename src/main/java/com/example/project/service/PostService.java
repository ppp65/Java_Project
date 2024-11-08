package com.example.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.model.Post;
import com.example.project.repository.PostRepository;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 게시글 저장
    @Transactional
    public Post savePost(Post post) {
        System.out.println("Saving post: " + post.getTitle());  // 로그 추가하여 확인
        return postRepository.save(post);  // 데이터베이스에 저장
    }

    // 모든 게시글 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();  // 모든 게시글 반환
    }
}