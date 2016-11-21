package vn.wse.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import vn.wse.lms.service.UserService;


@Configuration
@ImportResource(value = "classpath:spring/spring-security.xml")
public class SecurityConfig {
	@Bean
	public UserService userService() {
		return new UserService();
	} 
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new StandardPasswordEncoder();
	}

}