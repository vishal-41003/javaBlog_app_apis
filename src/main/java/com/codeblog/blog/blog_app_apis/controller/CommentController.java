package com.codeblog.blog.blog_app_apis.controller;

import com.codeblog.blog.blog_app_apis.payloads.ApiResponse;
import com.codeblog.blog.blog_app_apis.payloads.CommentDto;
import com.codeblog.blog.blog_app_apis.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // Create Comment
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @Valid @RequestBody CommentDto commentDto,
            @PathVariable Integer postId) {

        log.info("Creating comment for postId={}", postId);

        CommentDto created = commentService.createComment(commentDto, postId);

        log.info("Comment created successfully with id={}", created.getId());

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Delete Comment
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {

        log.warn("Deleting comment with id={}", commentId);

        commentService.deleteComment(commentId);

        log.info("Comment deleted successfully with id={}", commentId);

        return ResponseEntity.noContent().build();
    }
}