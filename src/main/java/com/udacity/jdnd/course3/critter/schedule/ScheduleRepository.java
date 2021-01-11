package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    @Query(value = "SELECT DISTINCT s.id,s.date,s.created_at,s.updated_at\n" +
            "FROM schedule AS s\n" +
            "LEFT JOIN pet_schedule AS ps\n" +
            "\tON s.id = ps.schedule_id\n" +
            "LEFT JOIN pet AS p\n" +
            "\tON ps.pet_id = p.id\n" +
            "WHERE p.id = :petId",nativeQuery = true)
    List<Schedule> findSchedulesByPet (@Param(value = "petId") Long petId);

    @Query(value = "SELECT s.id,s.date,s.created_at,s.updated_at\n" +
            "FROM schedule AS s\n" +
            "LEFT JOIN employee_schedule AS es\n" +
            "\tON s.id = es.schedule_id\n" +
            "LEFT JOIN employee AS e\n" +
            "\tON es.employee_id = e.id\n" +
            "WHERE e.id = :employeeId",nativeQuery = true)
    List<Schedule> findSchedulesByEmployee (@Param(value = "employeeId") Long employeeId);

    @Query(value = "SELECT DISTINCT s.id,s.date,s.created_at,s.updated_at\n" +
            "FROM schedule AS s\n" +
            "LEFT JOIN pet_schedule AS ps\n" +
            "\tON s.id = ps.schedule_id\n" +
            "LEFT JOIN pet AS p\n" +
            "\tON ps.pet_id = p.id\n" +
            "WHERE p.owner_id = :customerId",nativeQuery = true)
    List<Schedule> findSchedulesByCustomer (@Param(value = "customerId") Long customerId);
}
