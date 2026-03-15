package com.project.telimedical.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.telimedical.entities.Hospital;

public interface HospitalRepository extends JpaRepository<Hospital,UUID> {
    
}
