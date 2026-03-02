package com.codeblog.blog.blog_app_apis.mapper;

import com.codeblog.blog.blog_app_apis.entities.Category;
import com.codeblog.blog.blog_app_apis.entities.Post;
import com.codeblog.blog.blog_app_apis.payloads.CategoryDto;
import com.codeblog.blog.blog_app_apis.payloads.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class PostMapper {

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final CommentMapper commentMapper;

    public PostDto toDto(Post post) {

        if (post == null) {
            return null;
        }

        PostDto dto = new PostDto();

        dto.setPostId(post.getPostId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setImageName(post.getImageName());
        dto.setAddedDate(post.getAddedDate());

        dto.setUser(userMapper.toDto(post.getUser()));
        dto.setCategory(categoryMapper.toDto(post.getCategory()));

        if (post.getComments() != null) {
            dto.setComments(
                    post.getComments()
                            .stream()
                            .map(commentMapper::toDto)
                            .collect(Collectors.toSet())
            );
        }

        return dto;
    }

    public Post toEntity(PostDto dto) {

        if (dto == null) {
            return null;
        }

        Post post = new Post();

        post.setPostId(dto.getPostId());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setImageName(dto.getImageName());
        post.setAddedDate(dto.getAddedDate());

        // We usually do NOT set user & category fully here
        // because they are fetched separately in service

        return post;
    }
}
