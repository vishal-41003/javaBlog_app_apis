package com.codeblog.blog.blog_app_apis.services;

import com.codeblog.blog.blog_app_apis.entities.Category;
import com.codeblog.blog.blog_app_apis.payloads.CategoryDto;

import java.util.List;

public interface CategoryService {

    //create
    CategoryDto createCategory(CategoryDto categoryDto);

    //Update
    CategoryDto updateCategory(CategoryDto categoryDto , Integer categoryId);

    //Delete
    void deleteCategory(Integer categoryId);

    //Get
    CategoryDto getCategory(Integer categoryId);

    //GetAll
    List<CategoryDto> getCategories();
}
