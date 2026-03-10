package com.codeblog.blog.blog_app_apis.services;

import com.codeblog.blog.blog_app_apis.entities.Post;
import com.codeblog.blog.blog_app_apis.payloads.PostDto;
import com.codeblog.blog.blog_app_apis.payloads.PostResponse;

import java.util.List;

public interface PostService {
    //create
    PostDto createPost(PostDto postDto, String email, Integer categoryId);


    //Update
    PostDto updatePost(PostDto postDto, Integer postId);



    //delete
    void deletePost(Integer postId);

    //getAllPost
    PostResponse getAllPost(Integer pageNumber, Integer pagesize,String sortBy,String sortDir);

    //getPostById
    PostDto getPostById(Integer postId);

    //getAllPostByCategory
    List<PostDto> getPostByCategory(Integer categoryId);

    //getAllPostByUser
    List<PostDto> getAllPostByUser(Integer userId);

    //searchPost
    List<PostDto> searchPost(String keyword);
}
