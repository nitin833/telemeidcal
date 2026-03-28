package com.project.telimedical;


import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import com.project.telimedical.Exceptions.UserNotFoundExceptions;
import com.project.telimedical.HelperClasses.DoctorCreatorObject;
import com.project.telimedical.entities.DoctorProfile;
import com.project.telimedical.entities.Qualification;
import com.project.telimedical.entities.Role;
import com.project.telimedical.entities.Users;
import com.project.telimedical.services.UserService;

@SpringBootApplication
@EnableCaching
@EnableMethodSecurity
public class TelimedicalApplication {


	public static void main(String[] args) {
		SpringApplication.run(TelimedicalApplication.class, args);

	}

	


}
