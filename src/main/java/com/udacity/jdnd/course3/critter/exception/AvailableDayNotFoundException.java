package com.udacity.jdnd.course3.critter.exception;

public class AvailableDayNotFoundException extends RuntimeException{
    public AvailableDayNotFoundException (String message){
        super(message);
    }
}
