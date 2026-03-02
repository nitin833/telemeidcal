package com.project.telimedical.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="appointments")
public class Appointments {
    
     @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID appointment_id;

    @Column(name = "status",nullable = false)
    private String status;



    @ManyToOne
    @JoinColumn(name="patient",nullable = false)
    private Users patient;

     @ManyToOne
    @JoinColumn(name="doctor",nullable = false)
    private Users doctor;

    @OneToOne(cascade = CascadeType.ALL)
    private Prescription prescription;

    private LocalDateTime starTime;

    public LocalDateTime getStarTime() {
        return starTime;
    }

    public void setStarTime(LocalDateTime starTime) {
        this.starTime = starTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    private LocalDateTime endTime;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Users getPatient() {
        return patient;
    }

    public void setPatient(Users patient) {
        this.patient = patient;
    }

    public Users getDoctor() {
        return doctor;
    }

    public void setDoctor(Users doctor) {
        this.doctor = doctor;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public Appointments() {
    }

    public Appointments(String status, Users patient, Users doctor) {
        this.status = status;
        this.patient = patient;
        this.doctor = doctor;
    }

    public UUID getAppointment_id() {
        return appointment_id;
    }


    


}
