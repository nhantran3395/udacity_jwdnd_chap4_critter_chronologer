package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.exception.ActivityNotFoundException;
import com.udacity.jdnd.course3.critter.exception.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.exception.PetNotFoundException;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
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

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = this.convertToEntity(scheduleDTO);
        schedule.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return this.convertToDTO(scheduleService.addSchedule(schedule));

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
        {
            if(context.getSource() == null){
                return null;
            }

            return context.getSource()
                    .stream()
                    .map(Activity::getActivity)
                    .collect(Collectors.toSet());
        };

        final Converter<Set<SkillEnum>,Set<Activity>> skillEnumToActivityEntityConverter = context ->
        {
            if(context.getSource() == null){
                return null;
            }

            return  context.getSource()
                    .stream()
                    .map(activity -> activityRepository.findByActivity(activity).orElseThrow(() -> new ActivityNotFoundException("no such activity")))
                    .collect(Collectors.toSet());
        };

        final Converter<List<Pet>,List<Long>> petEntityToPetIdConverter = context ->{
            if(context.getSource() == null){
                return null;
            }

            return context.getSource()
                    .stream()
                    .map(Pet::getId)
                    .collect(Collectors.toList());
        };

        final Converter<List<Long>,List<Pet>> petIdToPetEntityConverter = context ->{
            if(context.getSource() == null){
                return null;
            }

            return context.getSource()
                    .stream()
                    .map(petId -> petRepository.findById(petId).orElseThrow(() -> new PetNotFoundException("no such pet")))
                    .collect(Collectors.toList());
        };

        final Converter<List<Employee>,List<Long>> employeeEntityToEmployeeIdConverter = context ->{
            if(context.getSource() == null){
                return null;
            }

            return context.getSource()
                    .stream()
                    .map(Employee::getId)
                    .collect(Collectors.toList());
        };

        final Converter<List<Long>,List<Employee>> employeeIdToEmployeeEntityConverter = context ->
        {
            if(context.getSource() == null){
                return null;
            }

            return context.getSource()
                    .stream()
                    .map(employeeId -> employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException("no such employee")))
                    .collect(Collectors.toList());
        };

        modelMapper.typeMap(Schedule.class, ScheduleDTO.class)
                .addMappings(mapper -> mapper.using(activityEntityToSkillEnumConverter).map(Schedule::getActivities, ScheduleDTO::setActivities))
                .addMappings(mapper -> mapper.using(petEntityToPetIdConverter).map(Schedule::getPets,
                        ScheduleDTO::setPetIds))
                .addMappings(mapper -> mapper.using(employeeEntityToEmployeeIdConverter).map(Schedule::getEmployees,
                        ScheduleDTO::setEmployeeIds));

        modelMapper.typeMap(ScheduleDTO.class,Schedule.class)
                .addMappings(mapper -> mapper.using(skillEnumToActivityEntityConverter).map(ScheduleDTO::getActivities, Schedule::setActivities))
                .addMappings(mapper -> mapper.using(petIdToPetEntityConverter).map(ScheduleDTO::getPetIds,
                        Schedule::setPets))
                .addMappings(mapper -> mapper.using(employeeIdToEmployeeEntityConverter).map(ScheduleDTO::getEmployeeIds,
                        Schedule::setEmployees));

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
