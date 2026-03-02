package com.codeblog.blog.blog_app_apis.controller;

import com.codeblog.blog.blog_app_apis.payloads.ApiResponse;
import com.codeblog.blog.blog_app_apis.payloads.CommentDto;
import com.codeblog.blog.blog_app_apis.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        CommentDto created = commentService.createComment(commentDto, postId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Delete Comment
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
