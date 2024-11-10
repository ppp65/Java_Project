package com.example.project.controller;

import com.example.project.model.Post;
import com.example.project.model.User;
import com.example.project.repository.PostRepository;
import com.example.project.repository.UserRepository;
import com.example.project.service.CustomUserDetails;
import com.example.project.service.CustomUserDetailsService;
import com.example.project.service.PostService;  // PostService 추가
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PostService postService;  // PostService 주입

    // 게시글 생성
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createPost(@RequestBody Post post) {
        Map<String, String> response = new HashMap<>();
        try {
            // 인증된 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            // userId로 사용자 찾기
            User user = userRepository.findByUserId(userDetails.getUserId()).orElse(null);

            if (user != null) {
                post.setUser(user);  // 사용자 설정
                postService.savePost(post);  // PostService의 savePost 호출
                response.put("message", "게시글이 성공적으로 작성되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "사용자를 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("message", "게시글 작성 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }

    // 게시글 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return ResponseEntity.ok(posts);
    }

    // 게시글 조회 (상세보기)
    @GetMapping
    public List<Post> getPosts() {
        return postService.getAllPosts();
    }

    // 게시글 상세보기 조회
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 게시글이 없으면 404 반환
        }
    }

    // 게시글 수정
    @PostMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        Map<String, String> response = new HashMap<>();
        try {
            // 게시글 ID로 게시글 찾기
            Post post = postRepository.findById(id).orElse(null);
            if (post != null) {
                // 게시글 작성자만 수정 가능하도록 하기
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                // 작성자 userId와 일치하는지 확인
                if (!post.getUser().getUserId().equals(userDetails.getUserId())) {
                    response.put("message", "게시글 작성자만 수정할 수 있습니다.");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
                }
                post.setTitle(updatedPost.getTitle());
                post.setContent(updatedPost.getContent());
                postRepository.save(post);

                response.put("message", "게시글이 성공적으로 수정되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "게시글을 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("message", "게시글 수정 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deletePost(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            // 게시글 ID로 게시글 찾기
            Post post = postRepository.findById(id).orElse(null);
            if (post != null) {
                // 게시글 작성자만 삭제 가능하도록 하기
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                // 작성자 userId와 일치하는지 확인
                if (!post.getUser().getUserId().equals(userDetails.getUserId())) {
                    response.put("message", "게시글 작성자만 삭제할 수 있습니다.");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
                }
                postRepository.delete(post);
                response.put("message", "게시글이 성공적으로 삭제되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "게시글을 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("message", "게시글 삭제 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }

}