package com.project.telimedical.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.telimedical.entities.Hospital;
import com.project.telimedical.services.HospitalService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;
    

    @GetMapping("/")
    public List<Hospital> getHospitals(@RequestParam String param) {
        return hospitalService.getAllHospitals();
    }

    @GetMapping("/{id}")
    public Hospital getHospitalsBYId(@PathVariable String id) {
        return hospitalService.getHospitalById(java.util.UUID.fromString(id)); 
    }


    @PostMapping("/")
    public Hospital createHospital(@RequestBody Hospital hospital) {
        return hospitalService.createHospital(hospital);
    }

    @PutMapping("/addOwner/{hosid}/{ownerid}")
    public void addowner(@PathVariable String hosid, @PathVariable String ownerid) {
        hospitalService.AddOwnerToHospital( java.util.UUID.fromString(ownerid),java.util.UUID.fromString(hosid));
    }

    @PutMapping("/addDoctor/{hosid}/{doctorid}")
    public void adddocter(@PathVariable String id, @PathVariable String doctorid) {
        hospitalService.AddDoctorToHospital( java.util.UUID.fromString(doctorid),java.util.UUID.fromString(id));
    }

     @PutMapping("/changeDescription/{hosid}")
    public Hospital changeDescription(@PathVariable String id,@RequestParam String desc) {
        return hospitalService.changeDescription(java.util.UUID.fromString(id),desc);
    }

    @PutMapping("removeOwner/{hosid}/{ownerid}")
    public void putMethodName(@PathVariable String id, @PathVariable String ownerid) {
        hospitalService.removeOwner( java.util.UUID.fromString(ownerid),java.util.UUID.fromString(id));
    
      
    }

        @PutMapping("removeDoctor/{hosid}/{doctorid}")
    public void removeDoctor(@PathVariable String id, @PathVariable String doctorid) {
        hospitalService.removeDoctor( java.util.UUID.fromString(doctorid),java.util.UUID.fromString(id));
    }





}
