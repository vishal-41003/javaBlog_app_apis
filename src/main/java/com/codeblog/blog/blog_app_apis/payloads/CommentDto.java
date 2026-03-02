package com.codeblog.blog.blog_app_apis.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private  Integer id;
    @NotBlank(message = "Comment content is required")
    @Size(min = 2, max = 500)
    private String content;
}
