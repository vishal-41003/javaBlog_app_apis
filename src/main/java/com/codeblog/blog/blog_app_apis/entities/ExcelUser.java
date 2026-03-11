package com.codeblog.blog.blog_app_apis.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "excel_users")
public class ExcelUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name must contain only alphabets")
    private String name;

    @NotBlank(message = "Lastname is required")
    @Pattern(regexp = "^[A-Za-z']+$", message = "Lastname must contain only alphabets")
    private String lastname;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

    @Min(value = 15, message = "Age must be at least 15")
    @Max(value = 80, message = "Age must be less than or equal to 80")
    private int age;
}
