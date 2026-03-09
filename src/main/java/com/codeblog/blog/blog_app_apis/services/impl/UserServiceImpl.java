package com.codeblog.blog.blog_app_apis.services.impl;


import com.codeblog.blog.blog_app_apis.config.AppConstants;
import com.codeblog.blog.blog_app_apis.entities.Role;
import com.codeblog.blog.blog_app_apis.entities.User;
import com.codeblog.blog.blog_app_apis.exceptions.ResourceNotFoundException;
import com.codeblog.blog.blog_app_apis.mapper.UserMapper;
import com.codeblog.blog.blog_app_apis.payloads.UserDto;
import com.codeblog.blog.blog_app_apis.repository.RoleRepo;
import com.codeblog.blog.blog_app_apis.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.codeblog.blog.blog_app_apis.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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

        log.info("Registering new user with email={}", userDto.getEmail());

        User user = this.userMapper.dtoToUser(userDto);

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role;

        // Check if this is the first user
        if(userRepo.count() == 0){

            log.info("First user detected. Assigning ADMIN role");

            role = roleRepo.findById(AppConstants.ROLE_ADMIN)
                    .orElseThrow(() -> {
                        log.error("Admin role not found");
                        return new RuntimeException("Admin Role not found");
                    });

        } else {

            role = roleRepo.findById(AppConstants.ROLE_USER)
                    .orElseThrow(() -> {
                        log.error("User role not found");
                        return new RuntimeException("User Role not found");
                    });

        }

        user.getRoles().add(role);

        User savedUser = this.userRepo.save(user);

        log.info("User registered successfully with id={}", savedUser.getId());

        return this.userMapper.userToDto(savedUser);
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        log.info("Creating user with email={}", userDto.getEmail());

        if (userRepo.existsByEmail(userDto.getEmail())) {
            log.warn("Email already registered: {}", userDto.getEmail());
            throw new RuntimeException("Email already registered!");
        }

        User user = userMapper.dtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepo.save(user);

        log.info("User created successfully with id={}", savedUser.getId());

        return userMapper.userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {

        log.info("Updating user with id={}", userId);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id={}", userId);
                    return new ResourceNotFoundException("User", "Id", userId);
                });

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setAbout(userDto.getAbout());

        User updatedUser = userRepo.save(user);

        log.info("User updated successfully with id={}", userId);

        return userMapper.userToDto(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByID(Integer userId) {

        log.debug("Fetching user with id={}", userId);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id={}", userId);
                    return new ResourceNotFoundException("User", "Id", userId);
                });

        return userMapper.userToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {

        log.debug("Fetching all users from database");

        return userRepo.findAll()
                .stream()
                .map(userMapper::userToDto)
                .toList();
    }

    @Override
    public void deleteUser(Integer userId) {

        log.warn("Deleting user with id={}", userId);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id={}", userId);
                    return new ResourceNotFoundException("User", "Id", userId);
                });

        userRepo.delete(user);

        log.info("User deleted successfully with id={}", userId);
    }
}