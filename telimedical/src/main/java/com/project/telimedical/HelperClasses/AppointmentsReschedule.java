package com.project.telimedical.HelperClasses;

import java.time.LocalDateTime;

public class AppointmentsReschedule {
 
    LocalDateTime newStartTime;
    LocalDateTime newEndTime;

    public LocalDateTime getNewStartTime() {
        return newStartTime;
    }

    public void setNewStartTime(LocalDateTime newStartTime) {
        this.newStartTime = newStartTime;
    }

    public LocalDateTime getNewEndTime() {
        return newEndTime;
    }

    public void setNewEndTime(LocalDateTime newEndTime) {
        this.newEndTime = newEndTime;
    }

    public AppointmentsReschedule() {
    }

    public AppointmentsReschedule(LocalDateTime newStartTime, LocalDateTime newEndTime) {
        this.newStartTime = newStartTime;
        this.newEndTime = newEndTime;
    }

    

}