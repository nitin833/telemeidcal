package com.project.telimedical.configurations;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.telimedical.entities.Users;
import com.project.telimedical.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Implement logic to load user details from your data source (e.g., database)
        Optional<Users> userOptional = userRepository.findByUsername(username);
        // and return a CustomUserDetails object containing user information and authorities.
        if(!userOptional.isPresent()){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
      Users user = userOptional.get();

      List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
      return new User(user.getUsername(), user.getPassword(), authorities);

        // You can use the UserRepository to fetch user data based on the username.
        // Example:
        // Users user = userRepository.findByUsername(username);
        // if (user == null) {
        //     throw new UsernameNotFoundException("User not found with username: " + username);
        // }
        // return new CustomUserDetails(user);
    }
    



}
