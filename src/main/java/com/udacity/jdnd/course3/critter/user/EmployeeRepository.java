package com.udacity.jdnd.course3.critter.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Optional<Employee> findByUsername (String username);

    @Query(value = "SELECT DISTINCT\n" +
            "\te.id,\n" +
            "\te.username,\n" +
            "\te.name,\n" +
            "\te.created_at,\n" +
            "\te.updated_at\n" +
            "FROM employee AS e\n" +
            "LEFT JOIN employee_available_day AS ead\n" +
            "\tON e.id = ead.employee_id\n" +
            "LEFT JOIN available_day AS ad\n" +
            "\tON ead.available_day_id = ad.id\n" +
            "LEFT JOIN employee_skill AS es\n" +
            "\tON e.id = es.employee_id\n" +
            "LEFT JOIN skill AS s\n" +
            "\tON es.skill_id = s.id\n" +
            "WHERE ad.day=:day AND s.skill IN :skills",nativeQuery = true)
    List<Employee> findEmployeesAvailableOnGivenDateAndHaveSuitableSkill (@Param(value = "day") Integer day,@Param(value = "skills") Set<Integer> skills);

}
