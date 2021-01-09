package com.udacity.jdnd.course3.critter.user;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private SkillEnum skill;

    @ManyToMany(mappedBy = "skills", fetch = FetchType.LAZY)
    private Set<Employee> employees = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SkillEnum getSkill() {
        return skill;
    }

    public void setSkill(SkillEnum skill) {
        this.skill = skill;
    }

    public Skill() {
    }

    public Skill(SkillEnum skill) {
        this.skill = skill;
    }
}
