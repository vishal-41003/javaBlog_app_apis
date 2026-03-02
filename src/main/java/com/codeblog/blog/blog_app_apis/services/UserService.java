package com.codeblog.blog.blog_app_apis.services;

import com.codeblog.blog.blog_app_apis.payloads.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto user);

    UserDto updateUser(UserDto user, Integer userId);

    UserDto getUserByID(Integer userId);

    List<UserDto> getAllUsers();

    void deleteUser(Integer userId);
}