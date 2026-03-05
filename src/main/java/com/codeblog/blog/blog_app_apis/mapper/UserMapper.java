package com.codeblog.blog.blog_app_apis.mapper;

import com.codeblog.blog.blog_app_apis.entities.User;
import com.codeblog.blog.blog_app_apis.payloads.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // DTO → Entity
    public User dtoToUser(UserDto userDto) {

        User user = new User();

        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        return user;
    }

    // Entity → DTO
    public UserDto userToDto(User user) {

        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setAbout(user.getAbout());

        return dto;
    }
}
