package com.project.telimedical.controllers;

import com.project.telimedical.services.AppointmentSerivce;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.telimedical.HelperClasses.AddUser;
import com.project.telimedical.HelperClasses.AppointmentsReschedule;
import com.project.telimedical.HelperClasses.DoctorCreatorObject;
import com.project.telimedical.HelperClasses.LoginResponce;
import com.project.telimedical.entities.Appointments;
import com.project.telimedical.entities.DoctorProfile;
import com.project.telimedical.entities.Qualification;
import com.project.telimedical.entities.Role;
import com.project.telimedical.entities.Users;
import com.project.telimedical.services.UserService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityProperties.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.telimedical.util.UUIDHandler;


@RestController
@RequestMapping("/user")
public class userController {

    private final AppointmentSerivce appointmentSerivce;
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    userController(AppointmentSerivce appointmentSerivce) {
        this.appointmentSerivce = appointmentSerivce;
    }
    

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Users getUserById(@PathVariable String id){

        UUID uuid = UUIDHandler.convertToUUID(id);
        return userService.getUserById(uuid);
    }

    @PostMapping("/addUser")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public Users addCustomer(@RequestBody AddUser users) {
     // System.out.println(users.getEmail());
     System.out.println("in the controller");
        Users u = new Users();
          u.setEmail(users.getEmail());
          u.setPassword(passwordEncoder.encode(users.getPassword()));
          u.setUsername(users.getUsername());
          u.setRole(Role.PATIENT);
      
        return userService.createcustomer(u);
    }

      @PostMapping("/addDoctor")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public Users createdoctor(@RequestBody DoctorCreatorObject doc) {
      doc.setPassword(passwordEncoder.encode(doc.getPassword()));
        return userService.createDoctor(doc);
    }

  @PutMapping("/{id}/user")
  public Users updateUser(@PathVariable String id, @RequestBody Users user) {
    UUID uuid = UUIDHandler.convertToUUID(id);
    return userService.updateuser(user, uuid);
  }

  @PutMapping("/{id}/doctor")
  @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN') or hasRole('OWNER')")
  public DoctorProfile updateDoctorProfile(@PathVariable String id, @RequestBody DoctorProfile doctorProfile) {
    UUID uuid = UUIDHandler.convertToUUID(id);
    return userService.updateDocterProfile(doctorProfile, uuid);
  }

  

    
  @PutMapping("/{id}/add")
  public DoctorProfile addQualification(@PathVariable String id, @RequestBody Qualification qualification) {
    UUID uuid = UUIDHandler.convertToUUID(id);
    return userService.addQualificationById(qualification, uuid);
  }   
  
   @PutMapping("/{id}/remove")
  public DoctorProfile removeQualification(@PathVariable String id, @RequestBody Qualification qualification) {
    UUID uuid = UUIDHandler.convertToUUID(id);
    return userService.removeQualificationById(qualification, uuid);
  }   
  

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable String id) {
    UUID uuid = UUIDHandler.convertToUUID(id);
    userService.removeUserById(uuid);
  }



  
    



  


}
