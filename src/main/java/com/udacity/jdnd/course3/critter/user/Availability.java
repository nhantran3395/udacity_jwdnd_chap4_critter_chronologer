package com.udacity.jdnd.course3.critter.user;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "day_of_week")
    @NotNull
    private String dayOfWeek;

    @ManyToMany(mappedBy = "availableDays", fetch = FetchType.LAZY)
    private Set<Employee> employees = new HashSet<>();
}
