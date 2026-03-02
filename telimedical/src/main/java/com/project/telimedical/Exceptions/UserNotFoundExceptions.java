package com.project.telimedical.Exceptions;

public class UserNotFoundExceptions extends RuntimeException {

    public UserNotFoundExceptions(String message){
        super(message);
    }

   public  UserNotFoundExceptions(){
        super("user not found");
    }

    
}
