package com.codeblog.blog.blog_app_apis.services.impl;

import com.codeblog.blog.blog_app_apis.entities.Category;
import com.codeblog.blog.blog_app_apis.exceptions.ResourceNotFoundException;
import com.codeblog.blog.blog_app_apis.mapper.CategoryMapper;
import com.codeblog.blog.blog_app_apis.payloads.CategoryDto;
import com.codeblog.blog.blog_app_apis.repository.CategoryRepo;
import com.codeblog.blog.blog_app_apis.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        log.info("Creating category with title={}", categoryDto.getCategoryTitle());

        Category category = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryRepo.save(category);

        log.info("Category created successfully with id={}", savedCategory.getCategoryId());

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {

        log.info("Updating category with id={}", categoryId);

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category not found with id={}", categoryId);
                    return new ResourceNotFoundException("Category", "categoryId", categoryId);
                });

        category.setCategoryTitle(categoryDto.getCategoryTitle());
        category.setCategoryDescription(categoryDto.getCategoryDescription());

        Category updated = categoryRepo.save(category);

        log.info("Category updated successfully with id={}", categoryId);

        return categoryMapper.toDto(updated);
    }

    @Override
    public void deleteCategory(Integer categoryId) {

        log.warn("Deleting category with id={}", categoryId);

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category not found with id={}", categoryId);
                    return new ResourceNotFoundException("Category", "categoryId", categoryId);
                });

        categoryRepo.delete(category);

        log.info("Category deleted successfully with id={}", categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(Integer categoryId) {

        log.debug("Fetching category with id={}", categoryId);

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category not found with id={}", categoryId);
                    return new ResourceNotFoundException("Category", "categoryId", categoryId);
                });

        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories() {

        log.debug("Fetching all categories");

        return categoryRepo.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }
}