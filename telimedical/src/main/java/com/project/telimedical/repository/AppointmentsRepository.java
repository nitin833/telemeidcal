package com.project.telimedical.repository;

import java.util.UUID;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.telimedical.entities.Appointments;
import com.project.telimedical.entities.Users;

import java.util.List;


public interface AppointmentsRepository extends JpaRepository<Appointments,UUID> {

    List<Appointments> findByDoctor(Users doctor);

    List<Appointments> findByPatient(Users patient);

    List<Appointments> findByDoctorUserId(UUID user_id);
    
    List<Appointments> findByPatientUserId(UUID user_id);

    List<Appointments> findByDoctorUserIdAndPatientUserId(UUID doctorId, UUID patientId);

   
}
