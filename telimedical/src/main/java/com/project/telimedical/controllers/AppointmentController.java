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

import com.project.telimedical.util.UUIDHandler;


@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentSerivce appointmentSerivce;

   
    @Autowired
    private PrescriptionService prescriptionService;




    /*@GetMapping("/")
    public List<Appointments> getAllAppointments() {
        return appointmentsRepository.findAll();
    }*/

    @GetMapping("/byDoctor")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('OWNER')")
    public List<Appointments> getAppointmentsByDoctor(@RequestParam String doctorId) {
        UUID uuid = UUIDHandler.convertToUUID(doctorId);
        return appointmentSerivce.loadAppointmentsByDoctorId(uuid);
    }
    
  @GetMapping("/byPatient")
@PreAuthorize("hasRole('PATIENT')")
public List<Appointments> getAppointmentsByPatient(@RequestParam String patientId) {  

    UUID uuid = UUIDHandler.convertToUUID(patientId);

    return appointmentSerivce.loadAppointmentsByPatientId(uuid);
}
    @GetMapping("/byDoctorAndPatient")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('OWNER')")
    public List<Appointments> getAppointmentsByDoctorAndPatient(@RequestParam String doctorId,  @RequestParam String patientId) {
        UUID doctorUuid = UUIDHandler.convertToUUID(doctorId);
        UUID patientUuid = UUIDHandler.convertToUUID(patientId);
            return appointmentSerivce.getCommonAppointments(doctorUuid, patientUuid);
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('PATIENT')")
    public Appointments crAppointments(@RequestBody  AppointmentObject appointmentObject) {
        UUID patientUuid = UUIDHandler.convertToUUID(appointmentObject.getPatientId());
        UUID doctorUuid = UUIDHandler.convertToUUID(appointmentObject.getDoctorId());
        return appointmentSerivce.createAppointment(patientUuid, doctorUuid);
    }

    
    @PutMapping("/{id}/accept")
    @PreAuthorize("hasRole('DOCTOR')")
    public Appointments accAppointments(@PathVariable String id) {
        UUID appointmentUuid = UUIDHandler.convertToUUID(id);
        return appointmentSerivce.acceptAppointments(appointmentUuid);
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('DOCTOR') ")
    public Appointments caAppointments(@PathVariable String id) {
        UUID appointmentUuid = UUIDHandler.convertToUUID(id);
        return appointmentSerivce.cancelAppointments(appointmentUuid);
    }




    
  @PutMapping("/{id}/reschedule")
  @PreAuthorize("hasRole('DOCTOR') ")
  public Appointments rescheduleAppointment(@PathVariable String id, @RequestBody AppointmentsReschedule appointment) {
    UUID appointmentUuid = UUIDHandler.convertToUUID(id);
    return appointmentSerivce.reschedule(appointmentUuid, appointment.getNewStartTime(), appointment.getNewEndTime());
  }
    

  @PostMapping("/{aid}/prescription")
   @PreAuthorize("hasRole('DOCTOR') ")
  public Prescription postMethodName(@RequestBody Prescription prescription, @PathVariable String aid) {
    UUID appointmentUuid = UUIDHandler.convertToUUID(aid);
    return prescriptionService.savePrescription(prescription, appointmentUuid);
  }

  @DeleteMapping("/{pid}/prescription")
   @PreAuthorize("hasRole('DOCTOR') ")
    public void deleteMethodName(@PathVariable String pid) {
        UUID prescriptionUuid = UUIDHandler.convertToUUID(pid);
        prescriptionService.removePrescription(prescriptionUuid);
    }

    @PutMapping("/{pid}/prescription")
     @PreAuthorize("hasRole('DOCTOR') ")
    public Prescription putMethodName(@PathVariable String pid, @RequestBody Prescription prescription) {
        UUID prescriptionUuid = UUIDHandler.convertToUUID(pid);
        return prescriptionService.updatePrescription(prescriptionUuid, prescription);
     }
  

    

}
