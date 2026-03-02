package com.codeblog.blog.blog_app_apis.mapper;

import com.codeblog.blog.blog_app_apis.entities.Comment;
import com.codeblog.blog.blog_app_apis.payloads.CommentDto;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentDto toDto(Comment comment) {

        if (comment == null) {
            return null;
        }

        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());

        return dto;
    }

    public Comment toEntity(CommentDto dto) {

        if (dto == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setContent(dto.getContent());

        return comment;
    }
}
