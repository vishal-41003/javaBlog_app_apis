package com.codeblog.blog.blog_app_apis.payloads;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private int id;

    @NotEmpty
    @Size(min=4, message = "user must be min of 4 character !!")
    private String name;

    @Email(message = "Email address is not valid ")
    private String email;

    @NotEmpty
    @Size(min = 3 , max = 10 , message = "Password must be min of 3 char and max  of 10 cahars !!")
    private String password;

    @NotEmpty
    private String about;
}
