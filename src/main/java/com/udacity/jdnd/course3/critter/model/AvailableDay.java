package com.udacity.jdnd.course3.critter.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AvailableDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private DayOfWeek day;

    @ManyToMany(mappedBy = "availableDays", fetch = FetchType.LAZY)
    private Set<Employee> employees = new HashSet<>();

    public AvailableDay(@NotNull DayOfWeek day) {
        this.day = day;
    }

}
