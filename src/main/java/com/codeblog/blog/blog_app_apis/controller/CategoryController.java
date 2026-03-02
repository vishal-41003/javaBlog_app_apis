package com.codeblog.blog.blog_app_apis.controller;

import com.codeblog.blog.blog_app_apis.payloads.ApiResponse;
import com.codeblog.blog.blog_app_apis.payloads.CategoryDto;
import com.codeblog.blog.blog_app_apis.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CategoryDto categoryDto) {

        CategoryDto created = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Update
    @PutMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable Integer catId) {

        CategoryDto updated = categoryService.updateCategory(categoryDto, catId);
        return ResponseEntity.ok(updated);
    }

    // Delete
    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer catId) {
        categoryService.deleteCategory(catId);
        return ResponseEntity.noContent().build();
    }

    // Get by Id
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer catId) {
        return ResponseEntity.ok(categoryService.getCategory(catId));
    }

    // Get All
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }
}
