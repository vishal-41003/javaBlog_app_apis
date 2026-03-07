package com.codeblog.blog.blog_app_apis.security;

import com.codeblog.blog.blog_app_apis.entities.User;
import com.codeblog.blog.blog_app_apis.exceptions.ResourceNotFoundException;
import com.codeblog.blog.blog_app_apis.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String userName)
            throws UsernameNotFoundException {

        log.debug("Loading user by username/email: {}", userName);

        User user = this.userRepo.findByEmail(userName)
                .orElseThrow(() -> {
                    log.error("User not found with email={}", userName);
                    return new ResourceNotFoundException("User", "email", userName);
                });

        log.debug("User found with email={}", userName);

        return user;
    }
}
