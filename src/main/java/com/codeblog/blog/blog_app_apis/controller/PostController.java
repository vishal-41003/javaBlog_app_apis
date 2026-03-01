package com.codeblog.blog.blog_app_apis.controller;

import com.codeblog.blog.blog_app_apis.config.AppConstants;
import com.codeblog.blog.blog_app_apis.payloads.ApiResponse;
import com.codeblog.blog.blog_app_apis.payloads.PostDto;
import com.codeblog.blog.blog_app_apis.payloads.PostResponse;
import com.codeblog.blog.blog_app_apis.services.FileService;
import com.codeblog.blog.blog_app_apis.services.PostService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletResponse;
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
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String  path;

    //create
    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDto> createPost(
            @RequestBody PostDto postDto,
            @PathVariable Integer userId,
            @PathVariable Integer categoryId
    ){
        PostDto createPost= this.postService.createPost(postDto,userId,categoryId);
        return new ResponseEntity<PostDto>(createPost, HttpStatus.CREATED);
    }

    //getByUser
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDto>> getPostbyUser(@PathVariable Integer userId){
List<PostDto>posts =this.postService.getAllPostByUser(userId);
return  new ResponseEntity<List<PostDto>>(posts , HttpStatus.OK);
    }

    //getBycategory
    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<List<PostDto>> getBycategory(@PathVariable Integer categoryId){
        List<PostDto>posts=this.postService.getPostByCategory(categoryId);
        return  new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
    }

    //getAllPost
    @GetMapping("/posts")
    public ResponseEntity<PostResponse>getAllPost(
            @RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir
    ){
        PostResponse postResponse =this.postService.getAllPost(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<PostResponse>(postResponse,HttpStatus.OK);
    }
    //getPostDetailsById
    @GetMapping("/post/{postID}")
    public  ResponseEntity<PostDto>getPostById(@PathVariable Integer postId){
        PostDto postDto=this.postService.getPostById(postId);
        return new ResponseEntity<PostDto>(postDto,HttpStatus.OK);
    }

    //deletePost
    @DeleteMapping("/post/{postId}")
    public ApiResponse deletePost(@PathVariable Integer postId){
        this.postService.deletePost(postId);
        return new ApiResponse("Post delete Sucessfully",true);
    }

    //updatePost
    @PutMapping("/post/{postId}")
    public ResponseEntity<PostDto> updatedPost(@RequestBody PostDto postDto ,@PathVariable Integer postId){
        PostDto updatedPost=this.postService.UpdatePost(postDto,postId);
        return new ResponseEntity<>(updatedPost,HttpStatus.OK);
    }

    //Search
    @GetMapping("/posts/search/{keywords}")
    public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keywords") String keywords){
        List<PostDto> result=this.postService.searchPost(keywords);
        return  new ResponseEntity<List<PostDto>>(result,HttpStatus.OK);
    }

    //post image upload
    public ResponseEntity<PostDto> uplaodPostImage(
            @RequestParam("image")MultipartFile image,
            @PathVariable Integer postId
            )throws IOException {
            PostDto postDto= this.postService.getPostById(postId);
            String fileName= this.fileService.uploadImage(path,image);
            postDto.setImageName(fileName);
            PostDto updatePost = this.postService.UpdatePost(postDto,postId);
            return  new ResponseEntity<PostDto>(updatePost,HttpStatus.OK);
    }

    //method to serve files
    @GetMapping(value = "post/image/{imageName}",produces = MediaType.IMAGE_PNG_VALUE)
    public  void downloadImage(
            @PathVariable("imageName")String imageName,
            HttpServletResponse response
    )throws IOException{
        InputStream resource= this.fileService.getResource(path,imageName);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }


}

