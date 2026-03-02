package com.project.telimedical.HelperClasses;

import java.util.HashSet;
import java.util.List;

import com.project.telimedical.entities.Qualification;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;

public class DoctorCreatorObject {
    
 
    private String username;

    
    private String email;

   
    private String password;

    
   

     private HashSet<Qualification> qualifications;
 
    
    private String firstname;

    private String lastname;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

 

    public HashSet<Qualification> getQualifications() {
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

    


}
