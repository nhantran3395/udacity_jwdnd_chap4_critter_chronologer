package com.udacity.jdnd.course3.critter.util;

import com.udacity.jdnd.course3.critter.DTO.EmployeeDTO;
import com.udacity.jdnd.course3.critter.exception.AvailableDayNotFoundException;
import com.udacity.jdnd.course3.critter.exception.SkillNotFoundException;
import com.udacity.jdnd.course3.critter.model.AvailableDay;
import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.model.Skill;
import com.udacity.jdnd.course3.critter.model.model_enum.SkillEnum;
import com.udacity.jdnd.course3.critter.repository.AvailableDayRepository;
import com.udacity.jdnd.course3.critter.repository.SkillRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EmployeeModelMapperUtil {

    private final ModelMapper modelMapper;

    @Autowired
    private AvailableDayRepository availableDayRepository;

    @Autowired
    private SkillRepository skillRepository;

    public EmployeeModelMapperUtil(){

        this.modelMapper = new ModelMapper();

        final Converter<Set<Skill>,Set<SkillEnum>> skillEntityToSkillEnumConverter = context -> {
            if(context.getSource() == null){
                return null;
            }

            return context.getSource()
                    .stream()
                    .map(Skill::getSkill)
                    .collect(Collectors.toSet());
        };

        final Converter<Set<SkillEnum>,Set<Skill>> skillEnumToSkillEntityConverter = context ->{
            if(context.getSource() == null){
                return null;
            }

            return context.getSource()
                    .stream()
                    .map(skill -> skillRepository.findBySkill(skill).orElseThrow(() -> new SkillNotFoundException("no such skill")))
                    .collect(Collectors.toSet());
        };

        final Converter<Set<AvailableDay>,Set<DayOfWeek>> availableDayEntityToDayOfWeekEnumConverter= context -> {
            if(context.getSource() == null){
                return null;
            }

            return context.getSource()
                    .stream()
                    .map(AvailableDay::getDay)
                    .collect(Collectors.toSet());
        };

        final Converter<Set<DayOfWeek>,Set<AvailableDay>> dayOfWeekToAvaillabeDayEntityConverter= context -> {
            if(context.getSource() == null){
                return null;
            }

            return context.getSource()
                    .stream()
                    .map(day->availableDayRepository.findByDay(day).orElseThrow(() -> new AvailableDayNotFoundException("no such day")))
                    .collect(Collectors.toSet());
        };

        modelMapper.typeMap(Employee.class, EmployeeDTO.class)
                .addMappings(mapper -> mapper.using(skillEntityToSkillEnumConverter).map(Employee::getSkills,EmployeeDTO::setSkills))
                .addMappings(mapper -> mapper.using(availableDayEntityToDayOfWeekEnumConverter).map(Employee::getAvailableDays,EmployeeDTO::setDaysAvailable));

        modelMapper.typeMap(EmployeeDTO.class,Employee.class)
                .addMappings(mapper -> mapper.using(skillEnumToSkillEntityConverter).map(EmployeeDTO::getSkills,Employee::setSkills))
                .addMappings(mapper -> mapper.using(dayOfWeekToAvaillabeDayEntityConverter).map(EmployeeDTO::getDaysAvailable,Employee::setAvailableDays));
    }

    public EmployeeDTO convertToEmployeeDTO(Employee employee){

        if(Objects.isNull(employee)){
            return  null;
        }

        return modelMapper.map(employee,EmployeeDTO.class);
    }

    public Employee convertToEmployeeEntity(EmployeeDTO employeeDTO){

        if(Objects.isNull(employeeDTO)){
            return  null;
        }

        return modelMapper.map(employeeDTO,Employee.class);
    }

    public Set<AvailableDay> convertDayOfWeekEnumToAvailableDayEntity (Set <DayOfWeek> days){
        return days.stream()
                .map(day->availableDayRepository.findByDay(day).orElseGet(null))
                .collect(Collectors.toSet());
    }

    public Set<Skill> convertSkillEnumToSkillEntity (Set <SkillEnum> employeeSkills){
        return employeeSkills.stream()
                .map(employeeSkill -> skillRepository.findBySkill(employeeSkill).orElseGet(null))
                .collect(Collectors.toSet());
    }

}
