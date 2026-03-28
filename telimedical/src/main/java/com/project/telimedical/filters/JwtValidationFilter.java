package com.project.telimedical.filters;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidationFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain)
            throws IOException, ServletException {
      
                String jwt = request.getHeader("Authorization");
                if(jwt != null && jwt.startsWith("Bearer ")) {
                    jwt = jwt.substring(7);
                    try {

                    Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(jwt).getBody();
                    String username = claims.get("username", String.class); 
                    String authorities = claims.get("authorities", String.class);

                    Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                        authentication.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                    catch(Exception e) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Invalid JWT Token");
                        return;
                    }
            


                   filterChain.doFilter(request, response);

                }

        

    }

     protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return path.equals("/login");
}
}