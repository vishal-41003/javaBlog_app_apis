package com.codeblog.blog.blog_app_apis.controller;

import com.codeblog.blog.blog_app_apis.entities.User;
import com.codeblog.blog.blog_app_apis.payloads.ApiResponse;
import com.codeblog.blog.blog_app_apis.payloads.UserDto;
import com.codeblog.blog.blog_app_apis.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){

        log.info("Creating new user with email={}", userDto.getEmail());

        UserDto createUserDto = userService.createUser(userDto);

        log.info("User created successfully with email={}", createUserDto.getEmail());

        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @Valid @RequestBody UserDto userDto,
            @PathVariable("userId") Integer uid){

        log.info("Updating user with id={}", uid);

        UserDto updateUser = userService.updateUser(userDto, uid);

        log.info("User updated successfully with id={}", uid);

        return ResponseEntity.ok(updateUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer uid){

        log.warn("Deleting user with id={}", uid);

        userService.deleteUser(uid);

        log.info("User deleted successfully with id={}", uid);

        return ResponseEntity.ok(new ApiResponse("User Deleted Successfully", true));
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers(){

        log.debug("Fetching all users");

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable("userId") Integer uid){

        log.debug("Fetching user with id={}", uid);

        return ResponseEntity.ok(userService.getUserByID(uid));
    }
}