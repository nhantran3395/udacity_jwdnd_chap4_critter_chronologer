package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public Schedule addSchedule (Schedule schedule){
        return scheduleRepository.save(schedule);
    }

    List<Schedule>  getAllSchedules () {
        return scheduleRepository.findAll();
    }

    List<Schedule> getSchedulesForAPet (Long petId) {return scheduleRepository.findSchedulesByPet(petId);}

    List<Schedule> getSchedulesForAnEmployee (Long employeeId) {return scheduleRepository.findSchedulesByEmployee(employeeId);}

    List<Schedule> getSchedulesForACustomer (Long customerId) {return scheduleRepository.findSchedulesByCustomer(customerId);}
}
