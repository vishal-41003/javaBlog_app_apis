package com.codeblog.blog.blog_app_apis.controller;

import com.codeblog.blog.blog_app_apis.exceptions.ApiException;
import com.codeblog.blog.blog_app_apis.payloads.JwtAuthRequest;
import com.codeblog.blog.blog_app_apis.payloads.JwtAuthResponse;
import com.codeblog.blog.blog_app_apis.payloads.UserDto;
import com.codeblog.blog.blog_app_apis.security.JwtTokenHelper;
import com.codeblog.blog.blog_app_apis.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    private final JwtTokenHelper jwtTokenHelper;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(
            @RequestBody JwtAuthRequest request) throws Exception {

        log.info("Login request for user: {}", request.getUsername());

        authenticate(request.getUsername(), request.getPassword());

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getUsername());

        String token = jwtTokenHelper.generateToken(userDetails);

        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
        response.setUsername(userDetails.getUsername());

        // Extract role from authorities
        String role = userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        response.setRole(role);

        return ResponseEntity.ok(response);
    }
    private void authenticate(String username, String password) throws Exception {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        try{
            authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException e){
           log.error("Invalid login attempt for user: {}",username);
            throw  new ApiException("Invalid userName or password !!");
        }


    }

    //register new user api
    @PostMapping("/register")
    public  ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
        log.info("Registering New user:{}",userDto.getEmail());
        UserDto registeredUser=this.userService.registerNewUser(userDto);
        log.info("User registered successfully:{}",registeredUser.getEmail());
        return new ResponseEntity<UserDto>(registeredUser,HttpStatus.CREATED);
    }

}