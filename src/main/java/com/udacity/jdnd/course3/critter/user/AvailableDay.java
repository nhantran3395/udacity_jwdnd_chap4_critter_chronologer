package com.udacity.jdnd.course3.critter.user;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

@Entity
public class AvailableDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private DayOfWeek day;

    @ManyToMany(mappedBy = "availableDays", fetch = FetchType.LAZY)
    private Set<Employee> employees = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.day = dayOfWeek;
    }

    public AvailableDay() {
    }

    public AvailableDay(@NotNull DayOfWeek day) {
        this.day = day;
    }

}
