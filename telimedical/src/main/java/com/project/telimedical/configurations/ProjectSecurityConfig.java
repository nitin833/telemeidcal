package com.project.telimedical.configurations;


import java.util.Arrays;
import org.springframework.security.config.Customizer;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;

import com.project.telimedical.filters.JwtTokenGeneratorFilter;
import com.project.telimedical.filters.JwtValidationFilter;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class ProjectSecurityConfig {
    
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{


   

    /*http.cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    config.setAllowedHeaders(Arrays.asList("*"));
    config.setAllowCredentials(true);
    return config;
    }))*/http.httpBasic(Customizer.withDefaults()).cors(cors->cors.disable())
   .csrf(csrf->csrf.disable()).
    authorizeHttpRequests((requests)->requests.
    requestMatchers("/user/addUser").permitAll()
    .requestMatchers("/hospital/**").hasRole("OWNER").
    requestMatchers("/user/**").authenticated()
    .requestMatchers("/hospital/**").authenticated()
    .requestMatchers("/appointments/**").authenticated()
    .requestMatchers("/login").authenticated());

    http.addFilterBefore(new JwtValidationFilter(), BasicAuthenticationFilter.class);
    http.addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class);
    
    
    http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        
        http.formLogin(fl->fl.disable());

        
        return http.build();
    }

  @Bean
  public PasswordEncoder passwordEncoder(){
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

 

}
