package com.udacity.jdnd.course3.critter.util;

import com.udacity.jdnd.course3.critter.errorhandling.EntityNotFoundException;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.model.Activity;
import com.udacity.jdnd.course3.critter.repository.ActivityRepository;
import com.udacity.jdnd.course3.critter.model.Schedule;
import com.udacity.jdnd.course3.critter.DTO.ScheduleDTO;
import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.model.model_enum.SkillEnum;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ScheduleModelMapperUtil {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    public ScheduleModelMapperUtil(){

        this.modelMapper = new ModelMapper();

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
                    .map(activity -> activityRepository.findByActivity(activity).orElseThrow(() -> new EntityNotFoundException(Activity.class,"activity",activity.toString())))
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
                    .map(petId -> petRepository.findById(petId).orElseThrow(() -> new EntityNotFoundException(Pet.class,"id",petId.toString())))
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
                    .map(employeeId -> employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException(Employee.class,"id",employeeId.toString())))
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

    public ScheduleDTO convertToDTO(Schedule schedule){

        if(Objects.isNull(schedule)){
            return null;
        }

        return modelMapper.map(schedule,ScheduleDTO.class);
    }

    public Schedule convertToEntity(ScheduleDTO scheduleDTO){

        if(Objects.isNull(scheduleDTO)){
            return null;
        }

        return modelMapper.map(scheduleDTO,Schedule.class);
    }
}
