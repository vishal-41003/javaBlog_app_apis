package com.codeblog.blog.blog_app_apis.controller;

import com.codeblog.blog.blog_app_apis.payloads.ApiResponse;
import com.codeblog.blog.blog_app_apis.payloads.CategoryDto;
import com.codeblog.blog.blog_app_apis.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //Create
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@Valid  @RequestBody CategoryDto categoryDto){
        CategoryDto createCategory= this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<CategoryDto>(createCategory, HttpStatus.CREATED);
    }
    //Update
    @PutMapping("/{catId}")
    public ResponseEntity<CategoryDto> updatecategory(@Valid @RequestBody CategoryDto categoryDto ,@PathVariable Integer catId){
        CategoryDto updatecategory=this.categoryService.updateCategory(categoryDto,catId);
        return  new ResponseEntity<CategoryDto>(updatecategory,HttpStatus.OK);
    }

    //Delete
    @DeleteMapping("/{catId}")
    public  ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer catId){
        this.categoryService.deletecategory(catId);
        return new ResponseEntity<ApiResponse>
                (new ApiResponse("Category is deleted Sucessfull",true),
                        HttpStatus.OK);
    }
    //Get
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getcategory(@PathVariable Integer catId){
        CategoryDto categoryDto= this.categoryService.getCategory(catId);
        return  new ResponseEntity<CategoryDto>(categoryDto,HttpStatus.OK);
    }

    //GetAll
    @GetMapping("/")
    public  ResponseEntity<List<CategoryDto>> getCategories(){
       List<CategoryDto> categories= this.categoryService.getCategories();
       return ResponseEntity.ok(categories);
    }
}
