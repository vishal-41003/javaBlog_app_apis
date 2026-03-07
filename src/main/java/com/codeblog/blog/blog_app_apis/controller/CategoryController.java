package com.codeblog.blog.blog_app_apis.controller;

import com.codeblog.blog.blog_app_apis.payloads.ApiResponse;
import com.codeblog.blog.blog_app_apis.payloads.CategoryDto;
import com.codeblog.blog.blog_app_apis.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CategoryDto categoryDto) {

        log.info("Creating category with title={}", categoryDto.getCategoryTitle());

        CategoryDto created = categoryService.createCategory(categoryDto);

        log.info("Category created successfully with id={}", created.getCategoryId());

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Update
    @PutMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable Integer catId) {

        log.info("Updating category with id={}", catId);

        CategoryDto updated = categoryService.updateCategory(categoryDto, catId);

        log.info("Category updated successfully with id={}", catId);

        return ResponseEntity.ok(updated);
    }

    // Delete
    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer catId) {

        log.warn("Deleting category with id={}", catId);

        categoryService.deleteCategory(catId);

        log.info("Category deleted successfully with id={}", catId);

        return ResponseEntity.noContent().build();
    }

    // Get by Id
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer catId) {

        log.debug("Fetching category with id={}", catId);

        return ResponseEntity.ok(categoryService.getCategory(catId));
    }

    // Get All
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {

        log.debug("Fetching all categories");

        return ResponseEntity.ok(categoryService.getCategories());
    }
}