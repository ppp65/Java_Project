package com.example.project.service;

import com.example.project.model.Post;
import com.example.project.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    public Iterable<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 특정 ID로 게시글 조회
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    // 이전과 다음 게시글 조회
    public Map<String, Post> getAdjacentPosts(Long postId) {
        Map<String, Post> adjacentPosts = new HashMap<>();

        Optional<Post> currentPost = postRepository.findById(postId);

        if (currentPost.isPresent()) {
            Post post = currentPost.get();

            // 이전 게시글 조회
            Post previousPost = postRepository.findFirstByCreatedAtBeforeOrderByCreatedAtDesc(post.getCreatedAt());
            if (previousPost != null) {
                adjacentPosts.put("previous", previousPost);
            }

            // 다음 게시글 조회
            Post nextPost = postRepository.findFirstByCreatedAtAfterOrderByCreatedAtAsc(post.getCreatedAt());
            if (nextPost != null) {
                adjacentPosts.put("next", nextPost);
            }
        }

        return adjacentPosts;
    }
}