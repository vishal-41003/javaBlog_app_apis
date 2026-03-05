package com.codeblog.blog.blog_app_apis.repository;

import com.codeblog.blog.blog_app_apis.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepo extends JpaRepository<Role,Integer> {
    List<Object> findById(String normalUser);

}
