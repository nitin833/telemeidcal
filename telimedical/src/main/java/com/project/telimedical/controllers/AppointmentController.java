package com.project.telimedical.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.project.telimedical.HelperClasses.AppointmentObject;
import com.project.telimedical.entities.Appointments;
import com.project.telimedical.repository.AppointmentsRepository;
import com.project.telimedical.services.AppointmentSerivce;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentSerivce appointmentSerivce;

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @GetMapping("/")
    public List<Appointments> getAllAppointments() {
        return appointmentsRepository.findAll();
    }

    @GetMapping("/byDoctor")
    public List<Appointments> getAppointmentsByDoctor(@RequestParam String doctorId) {
        return appointmentSerivce.loadAppointmentsByDoctorId(UUID.fromString(doctorId));
    }
    
    @GetMapping("/byPatient")
    public List<Appointments> getAppointmentsByPatient(@RequestParam String patientId) {   
        return appointmentSerivce.loadAppointmentsByPatientId(UUID.fromString(patientId));
    }

    @GetMapping("/byDoctorAndPatient")
    public List<Appointments> getAppointmentsByDoctorAndPatient(@RequestParam String doctorId,  @RequestParam String patientId) {
        return appointmentSerivce.getCommonAppointments(UUID.fromString(doctorId), UUID.fromString(patientId));
    }

    @PostMapping("/")
    public Appointments crAppointments(@RequestBody  AppointmentObject appointmentObject) {
        return appointmentSerivce.createAppointment(appointmentObject.getPatientId(), appointmentObject.getDoctorId());
    }

    @PutMapping("/{id}/accept")
    public Appointments accAppointments(@PathVariable String id) {
        return appointmentSerivce.acceptAppointments(UUID.fromString(id));
    }

    @PutMapping("/{id}/cancel")
    public Appointments caAppointments(@PathVariable String id) {
        return appointmentSerivce.cancelAppointments(UUID.fromString(id));
    }


    
    


    




    

}
