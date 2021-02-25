package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.model.Activity;
import com.udacity.jdnd.course3.critter.model.model_enum.SkillEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity,Long> {

    Optional<Activity> findByActivity (SkillEnum activity);
}
