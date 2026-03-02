package com.codeblog.blog.blog_app_apis.services.impl;


import com.codeblog.blog.blog_app_apis.entities.User;
import com.codeblog.blog.blog_app_apis.exceptions.ResourceNotFoundException;
import com.codeblog.blog.blog_app_apis.mapper.UserMapper;
import com.codeblog.blog.blog_app_apis.payloads.UserDto;
import com.codeblog.blog.blog_app_apis.repository.UserRepo;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.codeblog.blog.blog_app_apis.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepo.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        User user = userMapper.toEntity(userDto);

        User savedUser = userRepo.save(user);

        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "Id", userId));

        if (!user.getEmail().equals(userDto.getEmail()) &&
                userRepo.existsByEmail(userDto.getEmail())) {

            throw new RuntimeException("Email already registered!");
        }

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        User updatedUser = userRepo.save(user);

        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByID(Integer userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "Id", userId));

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {

        return userRepo.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void deleteUser(Integer userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "Id", userId));

        userRepo.delete(user);
    }
}