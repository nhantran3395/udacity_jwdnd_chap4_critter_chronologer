package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Optional<Employee> findByUsername (String username);

    @Query(value = "SELECT \n" +
            "\te1.id,\n" +
            "\te1.username,\n" +
            "\te1.name,\n" +
            "\te1.created_at,\n" +
            "\te1.updated_at\n" +
            "FROM employee AS e1\n" +
            "\tINNER JOIN \n" +
            "\t(\t\n" +
            "\tSELECT DISTINCT\n" +
            "\t\te.username\n" +
            "\tFROM employee AS e\n" +
            "\tLEFT JOIN employee_available_day AS ead\n" +
            "\t\tON e.id = ead.employee_id\n" +
            "\tLEFT JOIN available_day AS ad\n" +
            "\t\tON ead.available_day_id = ad.id\n" +
            "\tLEFT JOIN employee_skill AS es\n" +
            "\t\tON e.id = es.employee_id\n" +
            "\tLEFT JOIN skill AS s\n" +
            "\t\tON es.skill_id = s.id\n" +
            "\tWHERE ad.day = :day  AND s.skill IN :skills\n" +
            "\tGROUP BY e.username\n" +
            "\tHAVING COUNT(*) = :numOfDesiredSkills\n" +
            "\t) AS ums\n" +
            "\tON e1.username = ums.username\n"
            ,nativeQuery = true)
    List<Employee> findEmployeesAvailableOnGivenDateAndHaveSuitableSkill (@Param(value = "day") Integer day,@Param(value = "skills") Set<Integer> skills,@Param(value = "numOfDesiredSkills") Integer numOfDesiredSkills);

}
