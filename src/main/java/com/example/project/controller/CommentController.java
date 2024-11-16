package com.example.project.controller;

import com.example.project.DTO.CommentDTO;
import com.example.project.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 특정 게시글의 댓글 목록 조회
    @GetMapping
    public List<CommentDTO> getComments(@RequestParam Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    // 댓글 추가
    @PostMapping
    public ResponseEntity<String> addComment(@RequestBody CommentDTO commentDTO) {
        commentService.addComment(commentDTO);
        return ResponseEntity.ok("댓글이 추가되었습니다.");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @RequestParam String userId) {
        boolean isDeleted = commentService.deleteComment(commentId, userId);

        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build(); // 403: Forbidden, 작성자가 아니면 삭제 불가
        }
    }
}