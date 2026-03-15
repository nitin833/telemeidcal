package com.project.telimedical.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.telimedical.HelperClasses.DoctorCreatorObject;
import com.project.telimedical.entities.DoctorProfile;
import com.project.telimedical.entities.Qualification;
import com.project.telimedical.entities.Users;
import com.project.telimedical.services.UserService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/user")
public class userController {

    @Autowired
    private UserService userService;
    

    @GetMapping
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id")
    public Users getUserById(@PathVariable String id){

        UUID uuid = UUID.fromString(id);
        return userService.getUserById(uuid);
    }

    @PostMapping("/addUser")
    public Users addCustomer(@RequestBody Users users) {
        return userService.createcustomer(users);
    }

      @PostMapping("/addDoctor")
    public Users createdoctor(@RequestBody DoctorCreatorObject doc) {
        return userService.createDoctor(doc);
    }

  @PutMapping("/{id}/user")
  public Users updateUser(@PathVariable String id, @RequestBody Users user) {
    return userService.updateuser(user, UUID.fromString(id));
  }

  @PutMapping("/{id}/doctor")
  public DoctorProfile updateDoctorProfile(@PathVariable String id, @RequestBody DoctorProfile doctorProfile) {
    return userService.updateDocterProfile(doctorProfile, UUID.fromString(id));
  }

  

    
  @PutMapping("/{id}/add")
  public DoctorProfile addQualification(@PathVariable String id, @RequestBody Qualification qualification) {
    return userService.addQualificationById(qualification, UUID.fromString(id));
  }   
  
   @PutMapping("/{id}/remove")
  public DoctorProfile removeQualification(@PathVariable String id, @RequestBody Qualification qualification) {
    return userService.removeQualificationById(qualification, UUID.fromString(id));
  }   
  

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable String id) {
    userService.removeUserById(UUID.fromString(id));
  }
    

  


}
