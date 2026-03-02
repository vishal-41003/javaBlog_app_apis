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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final PostRepo postRepo;
    private final CommentRepo commentRepo;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post", "postId", postId));

        Comment comment = commentMapper.toEntity(commentDto);

        comment.setPost(post);

        Comment savedComment = commentRepo.save(comment);

        return commentMapper.toDto(savedComment);
    }

    @Override
    public void deleteComment(Integer commentId) {

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Comment", "commentId", commentId));

        commentRepo.delete(comment);
    }
}
