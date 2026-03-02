package com.project.telimedical.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


import com.project.telimedical.entities.Users;;


public interface UserRepository extends JpaRepository<Users,UUID> {


    
    
}
