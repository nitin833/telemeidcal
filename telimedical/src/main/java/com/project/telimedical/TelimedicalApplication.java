package com.project.telimedical;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import com.project.telimedical.entities.Role;
import com.project.telimedical.entities.Users;
import com.project.telimedical.repository.UserRepository;

@SpringBootApplication
@EnableCaching
@EnableMethodSecurity
public class TelimedicalApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelimedicalApplication.class, args);
	}

	@Bean
	CommandLineRunner seedAdminOnStartup(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			String adminUsername = "admin";
			String adminEmail = "admin@example.com";
			String rawPassword = "123456";

			List<Users> existingAdmins = userRepository.findByRole(Role.ADMIN);
			if (existingAdmins != null && !existingAdmins.isEmpty()) {
				userRepository.deleteAll(existingAdmins);
			}

			Users admin = new Users();
			admin.setUsername(adminUsername);
			admin.setEmail(adminEmail);
			admin.setPassword(passwordEncoder.encode(rawPassword));
			admin.setRole(Role.ADMIN);

			userRepository.save(admin);
			System.out.println("[Startup] Admin seeded: username=" + adminUsername + ", password=123456 (encoded)");
		};
	}
}

