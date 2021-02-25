package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.model.Schedule;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
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

    public List<Schedule>  getAllSchedules () {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getSchedulesForAPet (Long petId) {return scheduleRepository.findSchedulesByPet(petId);}

    public List<Schedule> getSchedulesForAnEmployee (Long employeeId) {return scheduleRepository.findSchedulesByEmployee(employeeId);}

    public List<Schedule> getSchedulesForACustomer (Long customerId) {return scheduleRepository.findSchedulesByCustomer(customerId);}
}
