package com.codeblog.blog.blog_app_apis.services.impl;

import com.codeblog.blog.blog_app_apis.entities.Comment;
import com.codeblog.blog.blog_app_apis.entities.Post;
import com.codeblog.blog.blog_app_apis.exceptions.ResourceNotFoundException;
import com.codeblog.blog.blog_app_apis.mapper.CommentMapper;
import com.codeblog.blog.blog_app_apis.payloads.CommentDto;
import com.codeblog.blog.blog_app_apis.repository.CommentRepo;
import com.codeblog.blog.blog_app_apis.repository.PostRepo;
import com.codeblog.blog.blog_app_apis.services.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final PostRepo postRepo;
    private final CommentRepo commentRepo;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId) {

        log.info("Creating comment for postId={}", postId);

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> {
                    log.error("Post not found with id={}", postId);
                    return new ResourceNotFoundException("Post", "postId", postId);
                });

        Comment comment = commentMapper.toEntity(commentDto);

        comment.setPost(post);

        Comment savedComment = commentRepo.save(comment);

        log.info("Comment created successfully with id={}", savedComment.getId());

        return commentMapper.toDto(savedComment);
    }

    @Override
    public void deleteComment(Integer commentId) {

        log.warn("Deleting comment with id={}", commentId);

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> {
                    log.error("Comment not found with id={}", commentId);
                    return new ResourceNotFoundException("Comment", "commentId", commentId);
                });

        commentRepo.delete(comment);

        log.info("Comment deleted successfully with id={}", commentId);
    }
}
