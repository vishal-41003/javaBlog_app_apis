package com.codeblog.blog.blog_app_apis.payloads;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private int id;

    @NotBlank(message = "Name is required")
    @Size(min = 4, max = 50, message = "Name must be between 4 and 50 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only letters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email address is not valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$",
            message = "Password must contain uppercase, lowercase, number and special character"
    )
    private String password;

    @NotBlank(message = "About is required")
    @Size(max = 500, message = "About cannot exceed 500 characters")
    private String about;

    private Set<RoleDto> roles=new HashSet<>();
}
