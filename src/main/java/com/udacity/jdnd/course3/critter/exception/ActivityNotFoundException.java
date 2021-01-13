package com.udacity.jdnd.course3.critter.exception;

public class ActivityNotFoundException extends RuntimeException{
    public ActivityNotFoundException (String message){
        super(message);
    }
}
