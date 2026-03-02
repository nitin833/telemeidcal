package com.project.telimedical.services;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.telimedical.Exceptions.UserNotFoundExceptions;
import com.project.telimedical.HelperClasses.DoctorCreatorObject;
import com.project.telimedical.entities.DoctorProfile;
import com.project.telimedical.entities.Qualification;
import com.project.telimedical.entities.Role;
import com.project.telimedical.entities.Users;
import com.project.telimedical.repository.DoctorProfileRepository;
import com.project.telimedical.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

      @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

   public List<Users> getAllUsers(){
    return userRepository.findAll();
   }

  public  Users getUserById(UUID id) throws UserNotFoundExceptions{
        if(id==null)
        return null;
    Optional<Users> op= userRepository.findById(id);
    if(op.isPresent()){
      return op.get();
    }
    else{
      throw new UserNotFoundExceptions("cannot get the user");
    }
   }

   @Transactional
  public Users createcustomer(Users user){
    return userRepository.save(user);
   }

   @Transactional
  public Users createDoctor(DoctorCreatorObject doctorCreatorObject){

    Users user = new Users();
    DoctorProfile doctorProfile = new DoctorProfile();

    user.setEmail(doctorCreatorObject.getEmail());
    user.setPassword(doctorCreatorObject.getPassword());
    user.setRole(Role.DOCTOR);
    user.setUsername(doctorCreatorObject.getUsername());

    userRepository.save(user);

    doctorProfile.setDoctor(user);
    doctorProfile.setFirstname(doctorCreatorObject.getFirstname());
    doctorProfile.setLastname(doctorCreatorObject.getLastname());
    doctorProfile.setQualifications(doctorCreatorObject.getQualifications());

    doctorProfile = doctorProfileRepository.save(doctorProfile);
    return getUserById(doctorProfile.getProfileid());
   }


   @Transactional
  public Users updateuser(Users user,UUID id)throws UserNotFoundExceptions{
    Users u1 = getUserById(id);
    u1.setEmail(user.getEmail());
    u1.setPassword(user.getPassword());
    u1.setUsername(user.getUsername());

    userRepository.save(u1);
    return u1;
   }

   @Transactional
  public DoctorProfile updateDocterProfile(DoctorProfile doctorProfile,UUID id) throws UserNotFoundExceptions{

    Optional<DoctorProfile> val = doctorProfileRepository.findById(id);
    if(val.isPresent()){

      DoctorProfile docp = val.get();
      docp.setFirstname(doctorProfile.getFirstname());
      docp.setLastname(doctorProfile.getLastname());
     return doctorProfileRepository.save(docp);
    }
    else{
      throw new UserNotFoundExceptions("docter profile not found");
    }
   }

   @Transactional
  public DoctorProfile addQualificationById(Qualification qualification,UUID id){
     Optional<DoctorProfile> val = doctorProfileRepository.findById(id);
    if(val.isPresent()){

      DoctorProfile docp = val.get();
      docp.getQualifications().add(qualification);
     return doctorProfileRepository.save(docp);
    }
    else{
      throw new UserNotFoundExceptions("docter profile not found");
    }
   }


    @Transactional
  public DoctorProfile removeQualificationById(Qualification qualification,UUID id){
     Optional<DoctorProfile> val = doctorProfileRepository.findById(id);
    if(val.isPresent()){

      DoctorProfile docp = val.get();
      docp.getQualifications().remove(qualification);
     return doctorProfileRepository.save(docp);
    }
    else{
      throw new UserNotFoundExceptions("docter profile not found");
    }
   }

   @Transactional
  public void removeUserById(UUID id){
      Optional<Users> val = userRepository.findById(id);
      if(val.isPresent()){
        Users u = val.get();
          if(u.getRole()==Role.PATIENT){
            userRepository.delete(u);
          }
          else{
            DoctorProfile dp = doctorProfileRepository.findById(id).get();
            doctorProfileRepository.delete(dp);
          }
      }
      else{
        throw new UserNotFoundExceptions("cannot delete because no such user exist with that id");
      }
   }

  
    
}
