package com.project.telimedical.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.annotation.Nonnull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctorProfile")
public class DoctorProfile {

    //this will not be generated
    @Id
    @Column(name = "profile_id")
    private UUID profileid;

    //it helps mapping doctor with profile
     @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "profile_id",nullable = false)
    private Users doctor;

    //to tell sql about lists of qualifications it only works on embeddable types so need to make Qualification embeddable
      @ElementCollection
    @CollectionTable(
        name = "doctor_qualifications",
        joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "qualifications",nullable = false)
    private Set<Qualification> qualifications = new HashSet<>();

   @Nonnull
    private String firstname;

    @Nonnull
    private String lastname;

    public UUID getProfileid() {
        return profileid;
    }

   

    public Users getDoctor() {
        return doctor;
    }

    public void setDoctor(Users doctor) {
        this.doctor = doctor;
    }

    public Set<Qualification> getQualifications() {
        return qualifications;
    }

    public void setQualifications(HashSet<Qualification> qualifications) {
        this.qualifications = qualifications;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }



    public DoctorProfile() {
    }



    public DoctorProfile(Users doctor, HashSet<Qualification> qualifications, String firstname, String lastname) {
        this.doctor = doctor;
        this.qualifications = qualifications;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    
 


   


}
