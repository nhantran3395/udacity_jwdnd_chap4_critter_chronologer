package com.udacity.jdnd.course3.critter.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class AvailableDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private DayOfWeek day;

    @ManyToMany(mappedBy = "availableDays", fetch = FetchType.LAZY)
    private Set<Employee> employees = new HashSet<>();

    public AvailableDay() {
    }

    public AvailableDay(@NotNull DayOfWeek day) {
        this.day = day;
    }

}
