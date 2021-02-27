package com.udacity.jdnd.course3.critter.DTO;

import com.udacity.jdnd.course3.critter.model.model_enum.SkillEnum;
import lombok.*;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private List<Long> employeeIds;

    @NotNull
    private List<Long> petIds;

    @NotNull
    @FutureOrPresent
    private LocalDate date;

    @NotNull
    private Set<SkillEnum> activities;
}
