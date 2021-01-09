package com.udacity.jdnd.course3.critter.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.Optional;

@Repository
public interface AvailableDayRepository extends JpaRepository<AvailableDay,Long> {

    Optional<AvailableDay> findByDay (DayOfWeek day);
}
