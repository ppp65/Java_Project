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
        System.out.println("Saving post: " + post.getTitle());
        return postRepository.save(post);
    }

    // 모든 게시글 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 특정 ID로 게시글 조회 (PostController에서 사용)
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);  // ID로 게시글 조회
    }
}