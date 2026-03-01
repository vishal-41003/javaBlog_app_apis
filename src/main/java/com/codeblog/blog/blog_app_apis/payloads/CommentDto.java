package com.codeblog.blog.blog_app_apis.payloads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private  int id;
    private String content;
}
