package com.project.telimedical.filters;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public class JwtValidationFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain)
            throws IOException, ServletException {
                 if (shouldNotFilter(request)) {
                    System.out.println("skipping the filter----------------------------------------------------------------");
            filterChain.doFilter(request, response);
            return;
        }


                System.out.println("inside the filter----------------------------------------------------------------");
      
                String jwt = request.getHeader("Token");
               // System.out.println(jwt);
                if(jwt != null && jwt.startsWith("Bearer ")) {
                    jwt = jwt.substring(7);
                    System.out.println(jwt);
                    try {

                    Claims claims = Jwts.parser().setSigningKey("qwertyuiopasdfghjklzxmnbvcxzaqwertuhbvcfghjiuytredcvbnkiuytrewsftyuikgredfghu").parseClaimsJws(jwt).getBody();
                    String username = claims.get("username", String.class); 
                    List<String> authorities = claims.get("authorities",List.class);

                    System.out.println(username);
                    System.out.println(authorities);

                    Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,authorities.stream().map(SimpleGrantedAuthority::new).toList());
                 
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("setting the authentication in the security context----------------------------------------------------------------");
                    }
                    catch(Exception e) {
                        System.out.println("Invalid JWT Token----------------------------------------------------------------"+e.getMessage());
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Invalid JWT Token");
                        return;
                    }
            
                        System.out.println("after setting the authentication in the security context----------------------------------------------------------------");

                   filterChain.doFilter(request, response);

                }
                else{
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid JWT Token");
                }

        

    }

     protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return path.equals("/login") || path.equals("/user/addUser");
}
}