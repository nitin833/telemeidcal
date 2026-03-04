package com.project.telimedical.Exceptions;

public class IncorrectDateException extends RuntimeException{
    
      public IncorrectDateException(String message){
        super(message);
    }
    public IncorrectDateException(){
        super("no appointment found");
    }
}
