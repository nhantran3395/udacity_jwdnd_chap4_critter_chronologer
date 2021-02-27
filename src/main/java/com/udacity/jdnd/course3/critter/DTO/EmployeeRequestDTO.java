package com.udacity.jdnd.course3.critter.DTO;

import com.udacity.jdnd.course3.critter.model.model_enum.SkillEnum;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

/**
 * Represents a request to find available employees by skills. Does not map
 * to the database directly.
 */
public class EmployeeRequestDTO {

    @NotNull
    private Set<SkillEnum> skills;

    @NotNull
    @FutureOrPresent
    private LocalDate date;

    public Set<SkillEnum> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillEnum> skills) {
        this.skills = skills;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
