package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.user.SkillEnum;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private SkillEnum activity;

    @ManyToMany(mappedBy = "activities", fetch = FetchType.LAZY)
    private Set<Schedule> schedules = new HashSet<>();


}
