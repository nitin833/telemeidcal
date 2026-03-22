package com.project.telimedical.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.project.telimedical.Exceptions.HospitalNotFoundException;
import com.project.telimedical.entities.Hospital;
import com.project.telimedical.entities.Users;
import com.project.telimedical.repository.HospitalRepository;


import jakarta.transaction.Transactional;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private UserService userService;
 
    @CacheEvict(value = "allHospitals", allEntries = true)
    @Transactional
    public Hospital createHospital(Hospital hs){
        return hospitalRepository.save(hs);
    }

    @Cacheable(value = "hospitalById", key = "#id")
    public Hospital getHospitalById(UUID id){
        Optional<Hospital> val  = hospitalRepository.findById(id);
        if(val.isEmpty())
            throw new HospitalNotFoundException();
        return val.get();
    }

    @Cacheable(value = "allHospitals")
    public List<Hospital> getAllHospitals(){
        return hospitalRepository.findAll();
    }

    @CacheEvict(value = {"hospitalById","allHospitals"}, allEntries = true)
    @Transactional
    public void AddOwnerToHospital(UUID owneUuid,UUID hUuid){
        Hospital h = getHospitalById(hUuid);
        h.getOwners().add(userService.getUserById(owneUuid));
        hospitalRepository.save(h);
    }

    @CacheEvict(value = {"hospitalById","allHospitals"}, allEntries = true)
    @Transactional
    public void AddDoctorToHospital(UUID doUuid,UUID hUuid){
        Hospital h = getHospitalById(hUuid);
        h.getDoctors().add(userService.getUserById(doUuid));
        hospitalRepository.save(h);
    }

    @CacheEvict(value = {"hospitalById","allHospitals"}, allEntries = true)
    @Transactional
    public Hospital changeDescription(UUID uid,String desc){
          Optional<Hospital> val  = hospitalRepository.findById(uid);
        if(val.isEmpty())
            throw new HospitalNotFoundException();
        Hospital hp = val.get();
        hp.setDescription(desc);
        return hospitalRepository.save(hp);
    }

    @CacheEvict(value = {"hospitalById","allHospitals"}, allEntries = true)
    @Transactional 
    public void removeDoctor(UUID hoUuid,UUID userid){
          Optional<Hospital> val  = hospitalRepository.findById(hoUuid);
        if(val.isEmpty())
            throw new HospitalNotFoundException();
        Hospital hp = val.get();
        hp.setDoctors(hp.getDoctors().stream().filter(u->u.getUserId()!=userid).toList());
        hospitalRepository.save(hp);
    }

        @CacheEvict(value = {"hospitalById","allHospitals"}, allEntries = true)
     @Transactional 
    public void removeOwner(UUID hoUuid,UUID userid){
          Optional<Hospital> val  = hospitalRepository.findById(hoUuid);
        if(val.isEmpty())
            throw new HospitalNotFoundException();
        Hospital hp = val.get();
        hp.setDoctors(hp.getOwners().stream().filter(u->u.getUserId()!=userid).toList());
        hospitalRepository.save(hp);
    }




}
