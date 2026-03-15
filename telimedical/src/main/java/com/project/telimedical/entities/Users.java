package com.project.telimedical.entities;

import java.util.List;
import java.util.UUID;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


 

@Entity
@Table(name="users")
public class Users {
 
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID user_id;

    @Column(name = "username",nullable = false,unique = true)
    private String username;

    @Column(name="email",nullable = false,unique = true)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(mappedBy = "patient",fetch = FetchType.LAZY)
    List<Appointments> patient_appointments;

    
   


    @OneToMany(mappedBy = "doctor",fetch = FetchType.LAZY)
    List<Appointments> doctor_appointments;

    public List<Hospital> getWorking_hospitals() {
        return working_hospitals;
    }


    public void setWorking_hospitals(List<Hospital> working_hospitals) {
        this.working_hospitals = working_hospitals;
    }


    public List<Hospital> getOwning_hospitals() {
        return owning_hospitals;
    }


    public void setOwning_hospitals(List<Hospital> owning_hospitals) {
        this.owning_hospitals = owning_hospitals;
    }


    @ManyToMany(mappedBy = "doctors")
    List<Hospital> working_hospitals;

    @ManyToMany(mappedBy = "owners")
    List<Hospital> owning_hospitals;
    


    public UUID getUserId() {
        return user_id;
    }


    public String getUsername() {
        return username;
    }


    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }


    


    public List<Appointments> getPatient_appointments() {
        return patient_appointments;
    }


    public List<Appointments> getDoctor_appointments() {
        return doctor_appointments;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public void setPassword(String password) {
        this.password = password;
    }


   


    public void setPatient_appointments(List<Appointments> patient_appointments) {
        this.patient_appointments = patient_appointments;
    }


    public void setDoctor_appointments(List<Appointments> doctor_appointments) {
        this.doctor_appointments = doctor_appointments;
    }


    public Users() {
    }


    public Role getRole() {
        return role;
    }


    public void setRole(Role role) {
        this.role = role;
    }


    public Users(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
       
    }

    

    


}
