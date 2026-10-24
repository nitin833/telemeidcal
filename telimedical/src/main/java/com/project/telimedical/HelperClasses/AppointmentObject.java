package com.project.telimedical.HelperClasses;

import java.util.UUID;

public class AppointmentObject {
    
    private String doctorId;
    private String patientId;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public AppointmentObject(String doctorId, String patientId) {
        this.doctorId = doctorId;
        this.patientId = patientId;
    }

    public AppointmentObject() {
    }



}
