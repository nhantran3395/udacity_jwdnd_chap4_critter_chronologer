package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.exception.EmployeeInvalidException;
import com.udacity.jdnd.course3.critter.exception.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.model.AvailableDay;
import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.repository.AvailableDayRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.model.model_enum.SkillEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AvailableDayRepository availableDayRepository;

    public Employee addEmployee (Employee employee) throws EmployeeInvalidException{
        employeeRepository.findByUsername(employee.getUsername()).ifPresent(e -> {throw new EmployeeInvalidException("username is duplicated");});
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeByID (Long employeeId) {
        return employeeRepository.findById(employeeId).orElse(null);
    }

    public Employee updateEmployeeAvailability (Set<AvailableDay> availableDays, Long employeeId) throws Exception {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException("no such employee"));
        employee.setAvailableDays(availableDays);

        return employeeRepository.save(employee);
    }

    public List<Employee> getEmployeesMatchedRequest (LocalDate date, Set<SkillEnum> skills){
        return employeeRepository.findEmployeesAvailableOnGivenDateAndHaveSuitableSkill(date.getDayOfWeek().ordinal(),
                skills.stream().map(SkillEnum::getValue).collect(Collectors.toSet()),
                skills.size());
    }
}