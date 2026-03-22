package com.project.telimedical.services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.telimedical.Exceptions.AppointmentNotFound;
import com.project.telimedical.Exceptions.IncorrectDateException;
import com.project.telimedical.Exceptions.UserNotFoundExceptions;
import com.project.telimedical.entities.Appointments;
import com.project.telimedical.entities.Users;
import com.project.telimedical.repository.AppointmentsRepository;
import com.project.telimedical.repository.UserRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@Service
public class AppointmentSerivce {
    

     @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private UserService userService;

    @Cacheable(value = "appointmentsByPatient", key = "#id")
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

    @Cacheable(value = "appointmentsByDoctor", key = "#id")
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

    @CacheEvict(value = {"appointmentsByPatient", "appointmentsByDoctor"}, allEntries = true)
    public Appointments createAppointment(UUID patientId,UUID doctorId){

        Appointments ap = new Appointments();
        
        Users d = userService.getUserById(doctorId);
        Users p = userService.getUserById(patientId);

        ap.setPatient(p);
        ap.setDoctor(d);

        ap.setStatus("PAID");
        return appointmentsRepository.save(ap);

    }

    @CacheEvict(value = {"appointmentsByPatient", "appointmentsByDoctor"},allEntries = true)
    public Appointments acceptAppointments(UUID appoUuid){

        Optional<Appointments> val = appointmentsRepository.findById(appoUuid);
        if(!val.isPresent()){
            throw new AppointmentNotFound("cannot accept appoinment due to no appointment found");
        }
        Appointments ap = val.get();
        ap.setStatus("ACCEPTED");
        return appointmentsRepository.save(ap);
    }

        @CacheEvict(value = {"appointmentsByPatient", "appointmentsByDoctor"},allEntries = true)
      public Appointments cancelAppointments(UUID appoUuid){

        Optional<Appointments> val = appointmentsRepository.findById(appoUuid);
        if(!val.isPresent()){
            throw new AppointmentNotFound("cannot cancel appoinment due to no appointment found");
        }
        Appointments ap = val.get();
        ap.setStatus("CANCELLED");
        return appointmentsRepository.save(ap);
    }

   /*  public Appointments refundAppointment(UUID appUuid){

         Optional<Appointments> val = appointmentsRepository.findById(appUuid);
        if(!val.isPresent()){
            throw new AppointmentNotFound("cannot refund appoinment due to no appointment found");
        }
        Appointments ap  =val.get();
        ap.setStatus("REFUNDED");
        return appointmentsRepository.save(ap);
    }*/

    @CacheEvict(value = {"appointmentsByPatient", "appointmentsByDoctor"}, allEntries = true)
    public Appointments reschedule(UUID appUuid,LocalDateTime updatedStarTime,LocalDateTime upDatedEndtime){

         Optional<Appointments> val = appointmentsRepository.findById(appUuid);
        if(!val.isPresent()){
            throw new AppointmentNotFound("cannot refund appoinment due to no appointment found");
        }
        Appointments ap  =val.get();
        if(!updatedStarTime.isBefore(upDatedEndtime)){
            throw new IncorrectDateException();
        }
        if(ap.getStarTime().isAfter(updatedStarTime)){
            throw new IncorrectDateException();
        }
        ap.setStarTime(updatedStarTime);
        ap.setEndTime(upDatedEndtime);

        return appointmentsRepository.save(ap);
    }    

   
    public List<Appointments> getCommonAppointments(UUID patientId,UUID doctorId){
        return appointmentsRepository.findByDoctorUserIdAndPatientUserId(doctorId, patientId);
    }





}
