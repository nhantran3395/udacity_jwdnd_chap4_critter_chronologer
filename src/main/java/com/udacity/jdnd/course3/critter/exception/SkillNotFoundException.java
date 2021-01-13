package com.udacity.jdnd.course3.critter.exception;

public class SkillNotFoundException extends RuntimeException{
    public SkillNotFoundException (String message){
        super(message);
    }
}
