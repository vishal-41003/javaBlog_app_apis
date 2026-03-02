package com.codeblog.blog.blog_app_apis.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

    private Integer categoryId;
    @NotBlank(message = "Category title is required")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    private String categoryTitle;

    @NotBlank(message = "Category description is required")
    @Size(min = 5, max = 200, message = "Description must be between 5 and 200 characters")
    private String categoryDescription;
}
