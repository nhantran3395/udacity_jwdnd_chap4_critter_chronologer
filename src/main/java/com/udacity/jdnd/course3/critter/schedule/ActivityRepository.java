package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.user.SkillEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity,Long> {

    Optional<Activity> findByActivity (SkillEnum activity);
}
