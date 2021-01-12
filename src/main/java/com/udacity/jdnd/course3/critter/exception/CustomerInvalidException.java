package com.udacity.jdnd.course3.critter.exception;

public class CustomerInvalidException extends RuntimeException{
    public CustomerInvalidException(String message){
        super(message);
    }
}
