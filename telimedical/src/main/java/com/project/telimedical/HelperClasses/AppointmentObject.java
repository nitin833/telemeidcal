package com.project.telimedical.HelperClasses;

import java.util.UUID;

public class AppointmentObject {
    
    private UUID doctorId;
    private UUID patientId;

    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public AppointmentObject() {
    
    }

    public AppointmentObject(UUID doctorId, UUID patientId) {
        this.doctorId = doctorId;
        this.patientId = patientId;
    }

}
