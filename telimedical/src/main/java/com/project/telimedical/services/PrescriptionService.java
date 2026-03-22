package com.project.telimedical.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.project.telimedical.Exceptions.AppointmentNotFound;
import com.project.telimedical.entities.Appointments;
import com.project.telimedical.entities.Prescription;
import com.project.telimedical.repository.AppointmentsRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class PrescriptionService {
    


    @Autowired 
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private EntityManager entityManager;

    
    @Transactional
    public Prescription savePrescription(Prescription pr,UUID apUuid){

        Optional<Appointments> val = appointmentsRepository.findById(apUuid);
        if(val.isEmpty())
            throw new AppointmentNotFound("appointment not found");

        Appointments ap= val.get();
        ap.setPrescription(pr);
        appointmentsRepository.save(ap);

        return ap.getPrescription();
    }

    @Transactional
    public void removePrescription(UUID uuid){

         Optional<Appointments> val = appointmentsRepository.findById(uuid);
        if(val.isEmpty())
            throw new AppointmentNotFound("appointment not found");

        Appointments ap= val.get();
        Prescription p = ap.getPrescription();
        ap.setPrescription(null);
        appointmentsRepository.save(ap);
        entityManager.remove(p);
    }


    @Transactional
    public Prescription updatePrescription(UUID uuid,Prescription prescription){

         Optional<Appointments> val = appointmentsRepository.findById(uuid);
        if(val.isEmpty())
            throw new AppointmentNotFound("appointment not found");

        Appointments ap= val.get();
        ap.setPrescription(prescription);
        appointmentsRepository.save(ap);

        return ap.getPrescription();
    }





}
