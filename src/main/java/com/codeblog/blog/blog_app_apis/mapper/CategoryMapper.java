package com.codeblog.blog.blog_app_apis.mapper;

import com.codeblog.blog.blog_app_apis.entities.Category;
import com.codeblog.blog.blog_app_apis.payloads.CategoryDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category) {

        if (category == null) {
            return null;
        }

        CategoryDto dto = new CategoryDto();
        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryTitle(category.getCategoryTitle());
        dto.setCategoryDescription(category.getCategoryDescription());

        return dto;
    }

    public Category toEntity(CategoryDto dto) {

        if (dto == null) {
            return null;
        }

        Category category = new Category();
        category.setCategoryId(dto.getCategoryId());
        category.setCategoryTitle(dto.getCategoryTitle());
        category.setCategoryDescription(dto.getCategoryDescription());

        return category;
    }
}