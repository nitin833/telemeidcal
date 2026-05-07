package com.project.telimedical.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import com.project.telimedical.HelperClasses.AppointmentObject;
import com.project.telimedical.HelperClasses.AppointmentsReschedule;
import com.project.telimedical.entities.Appointments;
import com.project.telimedical.entities.Prescription;
import com.project.telimedical.repository.AppointmentsRepository;
import com.project.telimedical.services.AppointmentSerivce;
import com.project.telimedical.services.PrescriptionService;

import org.springframework.web.bind.annotation.DeleteMapping;
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

    @Autowired
    private PrescriptionService prescriptionService;


    public UUID convertToUUID(String id) {

    if (id.startsWith("0x")) {
        id = id.substring(2); // remove 0x
    }

    id = id.toLowerCase();

    // format into UUID structure
    String formatted = id.substring(0, 8) + "-" +
                       id.substring(8, 12) + "-" +
                       id.substring(12, 16) + "-" +
                       id.substring(16, 20) + "-" +
                       id.substring(20);

    return UUID.fromString(formatted);
}

    /*@GetMapping("/")
    public List<Appointments> getAllAppointments() {
        return appointmentsRepository.findAll();
    }*/

    @GetMapping("/byDoctor")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('OWNER')")
    public List<Appointments> getAppointmentsByDoctor(@RequestParam String doctorId) {
        return appointmentSerivce.loadAppointmentsByDoctorId(UUID.fromString(doctorId));
    }
    
  @GetMapping("/byPatient")
@PreAuthorize("hasRole('PATIENT')")
public List<Appointments> getAppointmentsByPatient(@RequestParam String patientId) {  

    UUID uuid = convertToUUID(patientId);

    return appointmentSerivce.loadAppointmentsByPatientId(uuid);
}
    @GetMapping("/byDoctorAndPatient")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('OWNER')")
    public List<Appointments> getAppointmentsByDoctorAndPatient(@RequestParam String doctorId,  @RequestParam String patientId) {
        return appointmentSerivce.getCommonAppointments(UUID.fromString(doctorId), UUID.fromString(patientId));
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('PATIENT')")
    public Appointments crAppointments(@RequestBody  AppointmentObject appointmentObject) {
        return appointmentSerivce.createAppointment(appointmentObject.getPatientId(), appointmentObject.getDoctorId());
    }

    @PutMapping("/{id}/accept")
    @PreAuthorize("hasRole('DOCTOR')")
    public Appointments accAppointments(@PathVariable String id) {
        return appointmentSerivce.acceptAppointments(UUID.fromString(id));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('DOCTOR') ")
    public Appointments caAppointments(@PathVariable String id) {
        return appointmentSerivce.cancelAppointments(UUID.fromString(id));
    }




    
  @PutMapping("/{id}/reschedule")
  @PreAuthorize("hasRole('DOCTOR') ")
  public Appointments rescheduleAppointment(@PathVariable String id, @RequestBody AppointmentsReschedule appointment) {
    return appointmentSerivce.reschedule(UUID.fromString(id), appointment.getNewStartTime(), appointment.getNewEndTime());
  }
    

  @PostMapping("/{aid}/prescription")
   @PreAuthorize("hasRole('DOCTOR') ")
  public Prescription postMethodName(@RequestBody Prescription prescription, @PathVariable String aid) {
    return prescriptionService.savePrescription(prescription, UUID.fromString(aid));
  }

  @DeleteMapping("/{pid}/prescription")
   @PreAuthorize("hasRole('DOCTOR') ")
    public void deleteMethodName(@PathVariable String pid) {
        prescriptionService.removePrescription(UUID.fromString(pid));
    }

    @PutMapping("/{pid}/prescription")
     @PreAuthorize("hasRole('DOCTOR') ")
    public Prescription putMethodName(@PathVariable String pid, @RequestBody Prescription prescription) {
        return prescriptionService.updatePrescription(UUID.fromString(pid), prescription);
     }
  

    

}
