package com.project.telimedical.services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.telimedical.Exceptions.UserNotFoundExceptions;
import com.project.telimedical.entities.Appointments;
import com.project.telimedical.entities.Users;
import com.project.telimedical.repository.AppointmentsRepository;
import com.project.telimedical.repository.UserRepository;

@Service
public class AppointmentSerivce {
    

     @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private UserService userService;


    public List<Appointments> loadAppointmentsByPatientId(UUID id){
        List<Appointments> li = appointmentsRepository.findByPatientUserId(id);
        Collections.sort(li, new Comparator<Appointments>() {
    @Override
    public int compare(Appointments a1, Appointments a2) {
        LocalDateTime t1 = a1.getStarTime();
        LocalDateTime t2 = a2.getEndTime();
        if (t1 == null && t2 == null) return 0;
        if (t1 == null) return 1;   // nulls last
        if (t2 == null) return -1;
        return t1.compareTo(t2);
    }
});
return li;
    }


    public List<Appointments> loadAppointmentsByDoctorId(UUID id){
        List<Appointments> li = appointmentsRepository.findByDoctorUserId(id);
        Collections.sort(li, new Comparator<Appointments>() {
    @Override
    public int compare(Appointments a1, Appointments a2) {
        LocalDateTime t1 = a1.getStarTime();
        LocalDateTime t2 = a2.getEndTime();
        if (t1 == null && t2 == null) return 0;
        if (t1 == null) return 1;   // nulls last
        if (t2 == null) return -1;
        return t1.compareTo(t2);
    }
});
return li;
    }


    public Appointments createAppointment(UUID patientId,UUID doctorId){

        Appointments ap = new Appointments();
        
        Users d = userService.getUserById(doctorId);
        Users p = userService.getUserById(patientId);

        ap.setPatient(p);
        ap.setDoctor(d);

        ap.setStatus("PAID");
        return appointmentsRepository.save(ap);

    }






}
