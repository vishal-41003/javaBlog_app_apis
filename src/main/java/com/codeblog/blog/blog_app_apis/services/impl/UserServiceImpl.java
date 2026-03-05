package com.codeblog.blog.blog_app_apis.services.impl;


import com.codeblog.blog.blog_app_apis.config.AppConstants;
import com.codeblog.blog.blog_app_apis.entities.Role;
import com.codeblog.blog.blog_app_apis.entities.User;
import com.codeblog.blog.blog_app_apis.exceptions.ResourceNotFoundException;
import com.codeblog.blog.blog_app_apis.mapper.UserMapper;
import com.codeblog.blog.blog_app_apis.payloads.UserDto;
import com.codeblog.blog.blog_app_apis.repository.RoleRepo;
import com.codeblog.blog.blog_app_apis.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;

    @Override
    public UserDto registerNewUser(UserDto userDto) {

        User user = this.userMapper.dtoToUser(userDto);

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        Role role = this.roleRepo.findById(AppConstants.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);

        User savedUser = this.userRepo.save(user);

        return this.userMapper.userToDto(savedUser);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepo.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        User user = userMapper.dtoToUser(userDto);

        User savedUser = userRepo.save(user);

        return userMapper.userToDto(savedUser);
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

        return userMapper.userToDto(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByID(Integer userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "Id", userId));

        return userMapper.userToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {

        return userRepo.findAll()
                .stream()
                .map(userMapper::userToDto)
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