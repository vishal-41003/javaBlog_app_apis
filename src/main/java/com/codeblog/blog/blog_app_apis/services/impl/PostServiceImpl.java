package com.codeblog.blog.blog_app_apis.services.impl;

import com.codeblog.blog.blog_app_apis.mapper.PostMapper;
import com.codeblog.blog.blog_app_apis.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import com.codeblog.blog.blog_app_apis.entities.Category;
import com.codeblog.blog.blog_app_apis.entities.Post;
import com.codeblog.blog.blog_app_apis.entities.User;
import com.codeblog.blog.blog_app_apis.exceptions.ResourceNotFoundException;
import com.codeblog.blog.blog_app_apis.payloads.PostDto;
import com.codeblog.blog.blog_app_apis.payloads.PostResponse;
import com.codeblog.blog.blog_app_apis.repository.PostRepo;
import com.codeblog.blog.blog_app_apis.repository.UserRepo;
import com.codeblog.blog.blog_app_apis.services.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final PostMapper postMapper;

    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Post post = postMapper.toEntity(postDto);

        post.setImageName("default.png");
        post.setAddedDate(LocalDateTime.now());
        post.setUser(user);
        post.setCategory(category);

        Post savedPost = postRepo.save(post);

        return postMapper.toDto(savedPost);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        if (postDto.getImageName() != null) {
            post.setImageName(postDto.getImageName());
        }

        Post updatedPost = postRepo.save(post);

        return postMapper.toDto(updatedPost);
    }

    @Override
    public void deletePost(Integer postId) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));

        postRepo.delete(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getAllPost(Integer pageNumber, Integer pageSize,
                                   String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Post> pagePost = postRepo.findAll(pageable);

        List<PostDto> postDtos = pagePost.getContent()
                .stream()
                .map(postMapper::toDto)
                .toList();

        PostResponse response = new PostResponse();
        response.setContent(postDtos);
        response.setPageNumber(pagePost.getNumber());
        response.setPageSize(pagePost.getSize());
        response.setTotalElements(pagePost.getTotalElements());
        response.setTotalPages(pagePost.getTotalPages());
        response.setLastPage(pagePost.isLast());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostById(Integer postId) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));

        return postMapper.toDto(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getPostByCategory(Integer categoryId) {

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        return postRepo.findByCategory(category)
                .stream()
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getAllPostByUser(Integer userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        return postRepo.findByUser(user)
                .stream()
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> searchPost(String keyword) {

        return postRepo.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(postMapper::toDto)
                .toList();
    }
}