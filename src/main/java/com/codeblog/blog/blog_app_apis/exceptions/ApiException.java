package com.codeblog.blog.blog_app_apis.exceptions;

public class ApiException extends  RuntimeException{

    public ApiException(String message){
        super(message);
    }

    public ApiException(){
        super();
    }

}
