package com.project.telimedical.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.telimedical.entities.DoctorProfile;

public interface DoctorProfileRepository extends JpaRepository<DoctorProfile,UUID> {
    
}
