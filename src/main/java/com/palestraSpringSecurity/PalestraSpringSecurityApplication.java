package com.palestraSpringSecurity;

import com.palestraSpringSecurity.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class PalestraSpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(PalestraSpringSecurityApplication.class, args);
	}

}
