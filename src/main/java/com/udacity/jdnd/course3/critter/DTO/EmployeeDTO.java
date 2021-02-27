package com.udacity.jdnd.course3.critter.DTO;

import com.udacity.jdnd.course3.critter.model.model_enum.SkillEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.Set;

/**
 * Represents the form that employee request and response data takes. Does not map
 * to the database directly.
 */
@Getter
@Setter
@ToString
public class EmployeeDTO {
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String name;

    @NotNull
    private Set<SkillEnum> skills;

    @NotNull
    private Set<DayOfWeek> daysAvailable;
}
