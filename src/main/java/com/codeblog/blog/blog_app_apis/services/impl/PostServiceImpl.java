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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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

        log.info("Creating post for userId={} and categoryId={}", userId, categoryId);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id={}", userId);
                    return new ResourceNotFoundException("User", "userId", userId);
                });

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category not found with id={}", categoryId);
                    return new ResourceNotFoundException("Category", "categoryId", categoryId);
                });

        Post post = postMapper.toEntity(postDto);

        post.setImageName("default.png");
        post.setAddedDate(LocalDateTime.now());
        post.setUser(user);
        post.setCategory(category);

        Post savedPost = postRepo.save(post);

        log.info("Post created successfully with id={} and title={}", savedPost.getPostId(), savedPost.getTitle());

        return postMapper.toDto(savedPost);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {

        log.info("Updating post with id={}", postId);

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> {
                    log.error("Post not found with id={}", postId);
                    return new ResourceNotFoundException("Post", "postId", postId);
                });

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        if (postDto.getImageName() != null) {
            post.setImageName(postDto.getImageName());
        }

        Post updatedPost = postRepo.save(post);

        log.info("Post updated successfully with id={}", postId);

        return postMapper.toDto(updatedPost);
    }

    @Override
    public void deletePost(Integer postId) {

        log.info("Deleting post with id={}", postId);

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> {
                    log.error("Post not found with id={}", postId);
                    return new ResourceNotFoundException("Post", "postId", postId);
                });

        postRepo.delete(post);

        log.info("Post deleted successfully with id={}", postId);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getAllPost(Integer pageNumber, Integer pageSize,
                                   String sortBy, String sortDir) {

        log.debug("Fetching posts pageNumber={}, pageSize={}, sortBy={}, sortDir={}",
                pageNumber, pageSize, sortBy, sortDir);

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

        log.info("Fetched {} posts from database", postDtos.size());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostById(Integer postId) {

        log.debug("Fetching post by id={}", postId);

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> {
                    log.error("Post not found with id={}", postId);
                    return new ResourceNotFoundException("Post", "postId", postId);
                });

        return postMapper.toDto(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getPostByCategory(Integer categoryId) {

        log.debug("Fetching posts by categoryId={}", categoryId);

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category not found with id={}", categoryId);
                    return new ResourceNotFoundException("Category", "categoryId", categoryId);
                });

        List<PostDto> posts = postRepo.findByCategory(category)
                .stream()
                .map(postMapper::toDto)
                .toList();

        log.info("Found {} posts for categoryId={}", posts.size(), categoryId);

        return posts;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getAllPostByUser(Integer userId) {

        log.debug("Fetching posts for userId={}", userId);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id={}", userId);
                    return new ResourceNotFoundException("User", "userId", userId);
                });

        List<PostDto> posts = postRepo.findByUser(user)
                .stream()
                .map(postMapper::toDto)
                .toList();

        log.info("Found {} posts for userId={}", posts.size(), userId);

        return posts;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> searchPost(String keyword) {

        log.debug("Searching posts with keyword={}", keyword);

        List<PostDto> posts = postRepo.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(postMapper::toDto)
                .toList();

        log.info("Search completed. {} posts found for keyword={}", posts.size(), keyword);

        return posts;
    }
}