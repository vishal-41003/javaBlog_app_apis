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
		System.out.println(passwordEncoder.encode("vishal@123"));

		try{
			Role role=new Role();
			role.setId(AppConstants.ROLE_ADMIN);
			role.setName("ROLE_ADMIN");

			Role role1=new Role();
			role1.setId(AppConstants.ROLE_USER);
			role1.setName("ROLE_USER");

			List<Role>roles=List.of(role, role1);
			List<Role>result=this.roleRepo.saveAll(roles);

			result.forEach(r->{
				System.out.println(r.getName());
			});
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}