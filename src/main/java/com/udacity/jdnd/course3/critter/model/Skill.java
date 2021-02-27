package com.udacity.jdnd.course3.critter.model;

import com.udacity.jdnd.course3.critter.model.model_enum.SkillEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private SkillEnum skill;

    @ManyToMany(mappedBy = "skills", fetch = FetchType.LAZY)
    private Set<Employee> employees = new HashSet<>();
}
