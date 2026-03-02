package com.codeblog.blog.blog_app_apis.repository;

import com.codeblog.blog.blog_app_apis.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface CommentRepo extends JpaRepository<Comment,Integer> {
    }
