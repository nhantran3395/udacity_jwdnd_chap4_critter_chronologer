package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.model.Skill;
import com.udacity.jdnd.course3.critter.model.model_enum.SkillEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill,Long> {

    Optional<Skill> findBySkill (SkillEnum skill);
}