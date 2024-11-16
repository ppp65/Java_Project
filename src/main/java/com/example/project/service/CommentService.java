package com.example.project.service;

import com.example.project.repository.CommentRepository;
import com.example.project.DTO.CommentDTO;
import com.example.project.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    // 특정 게시글의 댓글 조회
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 댓글 추가
    public void addComment(CommentDTO commentDTO) {
        Comment comment = convertToEntity(commentDTO);
        commentRepository.save(comment);
    }

    // 댓글 삭제
    public boolean deleteComment(Long commentId, String userId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);

        if (comment != null && comment.getUserId().equals(userId)) {
            commentRepository.deleteById(commentId);
            return true;
        }

        return false; // 작성자가 아니거나 댓글이 없을 경우
    }

    // DTO -> Entity 변환
    private Comment convertToEntity(CommentDTO DTO) {
        Comment comment = new Comment();
        comment.setPostId(DTO.getPostId());
        comment.setUserId(DTO.getUserId());
        comment.setContent(DTO.getContent());
        return comment;
    }

    // Entity -> DTO 변환
    private CommentDTO convertToDto(Comment entity) {
        CommentDTO DTO = new CommentDTO();
        DTO.setId(entity.getId());
        DTO.setPostId(entity.getPostId());
        DTO.setUserId(entity.getUserId());
        DTO.setContent(entity.getContent());
        DTO.setCreatedAt(entity.getCreatedAt());
        return DTO;
    }
}