package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.Skill;
import com.udacity.jdnd.course3.critter.user.SkillEnum;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        throw new UnsupportedOperationException();
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.getAllSchedules()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return scheduleService.getSchedulesForAPet(petId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return scheduleService.getSchedulesForAnEmployee(employeeId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        return scheduleService.getSchedulesForACustomer(customerId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostConstruct
    private void postConstruct(){

        final Converter<Set<Activity>,Set<SkillEnum>> activityEntityToSkillEnumConverter = context ->
                context.getSource()
                        .stream()
                        .map(Activity::getActivity)
                        .collect(Collectors.toSet());

        final Converter<List<Pet>,List<Long>> petEntityToPetIdConverter = context ->
                context.getSource()
                        .stream()
                        .map(Pet::getId)
                        .collect(Collectors.toList());

        final Converter<List<Employee>,List<Long>> employeeEntityToEmployeeIdConverter = context ->
                context.getSource()
                        .stream()
                        .map(Employee::getId)
                        .collect(Collectors.toList());

        modelMapper.typeMap(Schedule.class, ScheduleDTO.class)
                .addMappings(mapper -> mapper.using(activityEntityToSkillEnumConverter).map(Schedule::getActivities, ScheduleDTO::setActivities))
                .addMappings(mapper -> mapper.using(petEntityToPetIdConverter).map(Schedule::getPets,
                        ScheduleDTO::setPetIds))
                .addMappings(mapper -> mapper.using(employeeEntityToEmployeeIdConverter).map(Schedule::getEmployees,
                        ScheduleDTO::setEmployeeIds));

    }

    private ScheduleDTO convertToDTO(Schedule schedule){

        if(Objects.isNull(schedule)){
            return null;
        }

        return modelMapper.map(schedule,ScheduleDTO.class);
    }

    private Schedule convertToEntity(ScheduleDTO scheduleDTO){

        if(Objects.isNull(scheduleDTO)){
            return null;
        }

        return modelMapper.map(scheduleDTO,Schedule.class);
    }
}
