package com.udacity.jdnd.course3.critter.DTO;

import com.udacity.jdnd.course3.critter.model.model_enum.SkillEnum;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Represents the form that schedule request and response data takes. Does not map
 * to the database directly.
 */
@Getter
@Setter
@ToString
public class ScheduleDTO {
    private Long id;
    private List<Long> employeeIds;
    private List<Long> petIds;
    private LocalDate date;
    private Set<SkillEnum> activities;
}
