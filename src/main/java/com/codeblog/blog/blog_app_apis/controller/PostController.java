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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    // CREATE
    @PostMapping("/users/{userId}/categories/{categoryId}/posts")
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody PostDto postDto,
            @PathVariable Integer userId,
            @PathVariable Integer categoryId
    ) {
        PostDto createdPost = postService.createPost(postDto, userId, categoryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // GET BY USER
    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<List<PostDto>> getPostByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(postService.getAllPostByUser(userId));
    }

    // GET BY CATEGORY
    @GetMapping("/categories/{categoryId}/posts")
    public ResponseEntity<List<PostDto>> getByCategory(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(postService.getPostByCategory(categoryId));
    }

    // GET ALL (Pagination + Sorting)
    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPost(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR) String sortDir
    ) {
        return ResponseEntity.ok(
                postService.getAllPost(pageNumber, pageSize, sortBy, sortDir)
        );
    }

    // GET BY ID
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    // DELETE
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok(new ApiResponse("Post deleted successfully", true));
    }

    // UPDATE
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(
            @Valid @RequestBody PostDto postDto,
            @PathVariable Integer postId) {

        return ResponseEntity.ok(postService.updatePost(postDto, postId));
    }

    // SEARCH
    @GetMapping("/posts/search")
    public ResponseEntity<List<PostDto>> searchPostByTitle(
            @RequestParam String keyword) {

        return ResponseEntity.ok(postService.searchPost(keyword));
    }

    // IMAGE UPLOAD
    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Integer postId
    ) throws IOException {

        PostDto postDto = this.postService.getPostById(postId);
        String fileName = this.fileService.uploadImage(path, image);
        postDto.setImageName(fileName);
        PostDto updatedPost = this.postService.updatePost(postDto, postId);

        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    // DOWNLOAD IMAGE
    @GetMapping(value = "/posts/image/{imageName}",
            produces = MediaType.IMAGE_PNG_VALUE)
    public void downloadImage(
            @PathVariable String imageName,
            HttpServletResponse response
    ) throws IOException {

        InputStream resource = fileService.getResource(path, imageName);

        response.setContentType(MediaType.IMAGE_PNG_VALUE);

        StreamUtils.copy(resource, response.getOutputStream());
    }
}

