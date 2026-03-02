package com.codeblog.blog.blog_app_apis.mapper;

import com.codeblog.blog.blog_app_apis.entities.User;
import com.codeblog.blog.blog_app_apis.payloads.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {

        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAbout(user.getAbout());

        return dto;
    }

    public User toEntity(UserDto dto) {

        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAbout(dto.getAbout());

        return user;
    }
}
