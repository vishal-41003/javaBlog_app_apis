package com.codeblog.blog.blog_app_apis.controller;

import com.codeblog.blog.blog_app_apis.config.AppConstants;
import com.codeblog.blog.blog_app_apis.payloads.ApiResponse;
import com.codeblog.blog.blog_app_apis.payloads.PostDto;
import com.codeblog.blog.blog_app_apis.payloads.PostResponse;
import com.codeblog.blog.blog_app_apis.services.FileService;
import com.codeblog.blog.blog_app_apis.services.PostService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    // CREATE
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/categories/{categoryId}")
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody PostDto postDto,
            @PathVariable Integer categoryId,
            Authentication authentication) {

        String email = authentication.getName();

        log.info("Creating post by user {} in category {}", email, categoryId);

        PostDto createdPost = postService.createPost(postDto, email, categoryId);

        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // GET BY USER
    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<List<PostDto>> getPostByUser(@PathVariable Integer userId) {

        log.info("Fetching posts for userId {}", userId);

        return ResponseEntity.ok(postService.getAllPostByUser(userId));
    }

    // GET BY CATEGORY
    @GetMapping("/categories/{categoryId}/posts")
    public ResponseEntity<List<PostDto>> getByCategory(@PathVariable Integer categoryId) {

        log.info("Fetching posts for categoryId {}", categoryId);

        return ResponseEntity.ok(postService.getPostByCategory(categoryId));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<PostResponse> getAllPost(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR) String sortDir) {

        log.info("Fetching all posts pageNumber {} pageSize {}", pageNumber, pageSize);

        return ResponseEntity.ok(
                postService.getAllPost(pageNumber, pageSize, sortBy, sortDir)
        );
    }

    // GET BY ID
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {

        log.info("Fetching post with id {}", postId);

        return ResponseEntity.ok(postService.getPostById(postId));
    }

    // DELETE
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId) {

        log.warn("Deleting post with id {}", postId);

        postService.deletePost(postId);

        return ResponseEntity.ok(new ApiResponse("Post deleted successfully", true));
    }

    // UPDATE
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(
            @Valid @RequestBody PostDto postDto,
            @PathVariable Integer postId) {

        log.info("Updating post with id {}", postId);

        return ResponseEntity.ok(postService.updatePost(postDto, postId));
    }

    // SEARCH
    @GetMapping("/search")
    public ResponseEntity<List<PostDto>> searchPostByTitle(
            @RequestParam String keyword) {

        log.info("Searching posts with keyword {}", keyword);

        return ResponseEntity.ok(postService.searchPost(keyword));
    }

    // IMAGE UPLOAD
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Integer postId) throws IOException {

        log.info("Uploading image for postId {}", postId);

        // get existing post
        PostDto postDto = this.postService.getPostById(postId);

        // upload image
        String fileName = this.fileService.uploadImage(path, image);

        // set image name
        postDto.setImageName(fileName);

        // update post
        PostDto updatedPost = this.postService.updatePost(postDto, postId);

        log.info("Image uploaded successfully for post {}", postId);

        return ResponseEntity.ok(updatedPost);
    }

    @GetMapping(value="/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable String imageName,
            HttpServletResponse response) throws IOException {

        InputStream resource = this.fileService.getResource(path, imageName);

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource, response.getOutputStream());
    }

}