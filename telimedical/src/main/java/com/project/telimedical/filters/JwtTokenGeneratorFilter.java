package com.project.telimedical.filters;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenGeneratorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,FilterChain filterChain)
            throws IOException, ServletException {
       
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if(authentication != null && authentication.isAuthenticated()) {
                 String jwt =   Jwts.builder().setSubject("JWT Token")
                   .claim("username", authentication.getName())
                   .claim("authorities", authentication.getAuthorities()).
                   signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, "secretkey").compact();  
                    response.addHeader("Authorization", "Bearer "+jwt);
                }
                

        filterChain.doFilter(request, response);
    }

        protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return !path.equals("/login");
        }
    
}
