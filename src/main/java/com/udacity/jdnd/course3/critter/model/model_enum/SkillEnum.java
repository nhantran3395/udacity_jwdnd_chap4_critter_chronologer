package com.udacity.jdnd.course3.critter.model.model_enum;

/**
 * A example list of employee skills that could be included on an employee or a schedule request.
 */
public enum SkillEnum {
    PETTING(0), WALKING(1), FEEDING(2), MEDICATING(3), SHAVING(4);

    private final int value;
    private SkillEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
