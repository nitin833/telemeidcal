package com.project.telimedical.filters;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public class JwtTokenGeneratorFilter extends OncePerRequestFilter {

  
   @Override
protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {

        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String jwt = Jwts.builder()
                .setSubject("JWT Token")
                .claim("username", authentication.getName())
                .claim("authorities", roles) // ✅ FIXED
                .signWith(SignatureAlgorithm.HS256,
                        "qwertyuiopasdfghjklzxmnbvcxzaqwertuhbvcfghjiuytredcvbnkiuytrewsftyuikgredfghu")
                .compact();

        response.addHeader("Authorization", "Bearer " + jwt);
    }

    System.out.println("after filter: " + authentication);
    System.out.println("JWT: " + response.getHeader("Authorization"));

    filterChain.doFilter(request, response);
}

        protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return !path.equals("/login");
        }
    
}
