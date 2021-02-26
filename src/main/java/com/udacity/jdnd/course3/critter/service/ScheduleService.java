package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.errorhandling.EntityNotFoundException;
import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.model.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
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

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Schedule addSchedule (Schedule schedule){
        return scheduleRepository.save(schedule);
    }

    public List<Schedule>  getAllSchedules () {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getSchedulesForAPet (Long petId) {
        if(!petRepository.findById(petId).isPresent()){
            throw new EntityNotFoundException(Pet.class,"id",petId.toString());
        }
        return scheduleRepository.findSchedulesByPet(petId);
    }

    public List<Schedule> getSchedulesForAnEmployee (Long employeeId) {
        if(!employeeRepository.findById(employeeId).isPresent()){
            throw new EntityNotFoundException(Employee.class,"id",employeeId.toString());
        }

        return scheduleRepository.findSchedulesByEmployee(employeeId);
    }

    public List<Schedule> getSchedulesForACustomer (Long customerId) {
        if(!customerRepository.findById(customerId).isPresent()){
            throw new EntityNotFoundException(Customer.class,"id",customerId.toString());
        }

        return scheduleRepository.findSchedulesByCustomer(customerId);}
}
