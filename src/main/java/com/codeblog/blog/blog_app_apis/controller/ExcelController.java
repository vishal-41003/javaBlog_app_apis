package com.codeblog.blog.blog_app_apis.controller;

import com.codeblog.blog.blog_app_apis.services.impl.ExcelServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/excel")
public class ExcelController {


    private final ExcelServiceImpl excelService;

    @PostMapping(value = "/upload",consumes = "multipart/form-data")
    public ResponseEntity<String> uploadExcel(@RequestParam("file")MultipartFile file){
        try {
            excelService.uploadExcel("uploads",file);
            return  ResponseEntity.ok("Excel uploaded Successfully");
        }catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
