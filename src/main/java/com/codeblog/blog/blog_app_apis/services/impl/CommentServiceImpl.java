package com.codeblog.blog.blog_app_apis.services.impl;

import com.codeblog.blog.blog_app_apis.entities.Comment;
import com.codeblog.blog.blog_app_apis.entities.Post;
import com.codeblog.blog.blog_app_apis.exceptions.ResourceNotFoundException;
import com.codeblog.blog.blog_app_apis.payloads.CommentDto;
import com.codeblog.blog.blog_app_apis.repository.CommentRepo;
import com.codeblog.blog.blog_app_apis.repository.PostRepo;
import com.codeblog.blog.blog_app_apis.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {


    private PostRepo postRepo;
    private CommentRepo commentRepo;
    private ModelMapper modelMapper;

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId) {
        Post post =this.postRepo.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));
        Comment comment= this.modelMapper.map(commentDto,Comment.class);
        comment.setPost(post);
        Comment savedComment= this.commentRepo.save(comment);
        return this.modelMapper.map(savedComment,CommentDto.class);
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment com= this.commentRepo.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","comment",commentId));
        this.commentRepo.delete(com);
    }
}
