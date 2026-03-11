package com.codeblog.blog.blog_app_apis.repository;

import com.codeblog.blog.blog_app_apis.entities.ExcelUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserExcelRepo extends JpaRepository<ExcelUser, Integer> {

    boolean existsByEmail(String email);

}