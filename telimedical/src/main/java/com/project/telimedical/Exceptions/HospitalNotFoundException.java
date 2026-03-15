package com.project.telimedical.Exceptions;

public class HospitalNotFoundException extends RuntimeException
 {
    
   public HospitalNotFoundException(String message){
        super(message);
    }
   public HospitalNotFoundException(){
        super("no hospital has found");
    }


    }
