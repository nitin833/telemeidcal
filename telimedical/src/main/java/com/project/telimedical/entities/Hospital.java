package com.project.telimedical.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "hosptal")
public class Hospital {
    
     @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID hosptal_id;

    @ManyToMany
    @JoinTable(
        name = "owner_hospital",
        joinColumns = @JoinColumn(name="hosptal_id"),
        inverseJoinColumns = @JoinColumn(name="user_id")
    )
    @Nonnull
    private List<Users> owners;

     @ManyToMany
    @JoinTable(
        name = "doctor_hospital",
        joinColumns = @JoinColumn(name="hosptal_id"),
        inverseJoinColumns = @JoinColumn(name="user_id")
    )
    private List<Users> doctors;

    private String Address;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

   
    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public List<Users> getOwners() {
        return owners;
    }

    public void setOwners(List<Users> owners) {
        this.owners = owners;
    }

    public List<Users> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Users> doctors) {
        this.doctors = doctors;
    }

   

    public UUID getHosptal_id() {
        return hosptal_id;
    }

    public Hospital(List<Users> owners, List<Users> doctors,String address) {
        this.owners = owners;
        this.doctors = doctors;
        this.Address=address;
    }

    


}
