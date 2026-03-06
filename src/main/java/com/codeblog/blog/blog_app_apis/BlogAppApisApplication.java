package com.codeblog.blog.blog_app_apis;

import com.codeblog.blog.blog_app_apis.config.AppConstants;
import com.codeblog.blog.blog_app_apis.entities.Role;
import com.codeblog.blog.blog_app_apis.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class BlogAppApisApplication implements CommandLineRunner {

	private final PasswordEncoder passwordEncoder;
	private final RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApisApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (roleRepo.count() == 0) {

			Role role = new Role();
			role.setId(AppConstants.ROLE_ADMIN);
			role.setName("ROLE_ADMIN");

			Role role1 = new Role();
			role1.setId(AppConstants.ROLE_USER);
			role1.setName("ROLE_USER");

			roleRepo.saveAll(List.of(role, role1));

			System.out.println("Default roles inserted successfully");
		}
	}
}