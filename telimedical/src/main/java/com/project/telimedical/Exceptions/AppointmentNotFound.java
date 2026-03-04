package com.project.telimedical.Exceptions;

public class AppointmentNotFound extends RuntimeException{

    public AppointmentNotFound(String message){
        super(message);
    }
    public AppointmentNotFound(){
        super("no appointment found");
    }
    
}
