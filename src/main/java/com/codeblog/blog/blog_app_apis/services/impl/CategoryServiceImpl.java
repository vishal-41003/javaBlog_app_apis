package com.codeblog.blog.blog_app_apis.services.impl;

import com.codeblog.blog.blog_app_apis.entities.Category;
import com.codeblog.blog.blog_app_apis.exceptions.ResourceNotFoundException;
import com.codeblog.blog.blog_app_apis.mapper.CategoryMapper;
import com.codeblog.blog.blog_app_apis.payloads.CategoryDto;
import com.codeblog.blog.blog_app_apis.repository.CategoryRepo;
import com.codeblog.blog.blog_app_apis.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        Category category = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryRepo.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));

        category.setCategoryTitle(categoryDto.getCategoryTitle());
        category.setCategoryDescription(categoryDto.getCategoryDescription());

        Category updated = categoryRepo.save(category);

        return categoryMapper.toDto(updated);
    }

    @Override
    public void deleteCategory(Integer categoryId) {

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));

        categoryRepo.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(Integer categoryId) {

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));

        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories() {

        return categoryRepo.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }
}