package com.project.telimedical.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.telimedical.entities.Hospital;
import com.project.telimedical.services.HospitalService;
import java.util.UUID;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import com.project.telimedical.util.UUIDHandler;


@RestController
@RequestMapping("/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;
    

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Hospital> getHospitals(@RequestParam String param) {
        return hospitalService.getAllHospitals();
    }

    @GetMapping("/{id}")
    public Hospital getHospitalsBYId(@PathVariable String id) {
        UUID hospitalUuid = UUIDHandler.convertToUUID(id);
        return hospitalService.getHospitalById(hospitalUuid); 
    }


    @PostMapping("/")
        @PreAuthorize("hasRole('ADMIN')")
    public Hospital createHospital(@RequestBody Hospital hospital) {
        return hospitalService.createHospital(hospital);
    }

    @PutMapping("/addOwner/{hosid}/{ownerid}")
    @PreAuthorize("hasRole('ADMIN')")
    public void addowner(@PathVariable String hosid, @PathVariable String ownerid) {
        UUID hospitalUuid = UUIDHandler.convertToUUID(hosid);
        UUID ownerUuid = UUIDHandler.convertToUUID(ownerid);
        hospitalService.AddOwnerToHospital( ownerUuid,hospitalUuid);
    }

    @PutMapping("/addDoctor/{hosid}/{doctorid}")
    public void adddocter(@PathVariable String hosid, @PathVariable String doctorid) {
        UUID hospitalUuid = UUIDHandler.convertToUUID(hosid);
        UUID doctorUuid = UUIDHandler.convertToUUID(doctorid);
        hospitalService.AddDoctorToHospital( doctorUuid,hospitalUuid);
    }

     @PutMapping("/changeDescription/{hosid}")
    public Hospital changeDescription(@PathVariable String hosid,@RequestParam String desc) {
        UUID hospitalUuid = UUIDHandler.convertToUUID(hosid);
        return hospitalService.changeDescription(hospitalUuid,desc);
    }

    @PutMapping("removeOwner/{hosid}/{ownerid}")
    @PreAuthorize("hasRole('ADMIN')")
    public void removeOwner(@PathVariable String hosid, @PathVariable String ownerid) {
        UUID hospitalUuid = UUIDHandler.convertToUUID(hosid);
        UUID ownerUuid = UUIDHandler.convertToUUID(ownerid);
        hospitalService.removeOwner( ownerUuid,hospitalUuid);
    
      
    }

        @PutMapping("removeDoctor/{hosid}/{doctorid}")
    public void removeDoctor(@PathVariable String hosid, @PathVariable String doctorid) {
        UUID   hospitalUuid = UUIDHandler.convertToUUID(hosid);
        UUID doctorUuid = UUIDHandler.convertToUUID(doctorid);
        hospitalService.removeDoctor( doctorUuid,hospitalUuid);
    }





}
