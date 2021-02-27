package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.model.Schedule;
import com.udacity.jdnd.course3.critter.DTO.ScheduleDTO;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.util.ScheduleModelMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
@Slf4j
public class ScheduleController {

    @Autowired
    private ScheduleModelMapperUtil scheduleModelMapperUtil;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody @Valid ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleModelMapperUtil.convertToEntity(scheduleDTO);
        schedule.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        ScheduleDTO scheduleDTOReturned = scheduleModelMapperUtil.convertToDTO(scheduleService.addSchedule(schedule));

        log.info("POST /schedule");
        log.info("Create a new schedule");
        log.info(scheduleDTO.toString());
        log.info(scheduleDTOReturned != null ? scheduleDTOReturned.toString() : null);

        return scheduleDTOReturned;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<ScheduleDTO> scheduleDTOs = scheduleService.getAllSchedules()
                .stream()
                .map(scheduleModelMapperUtil::convertToDTO)
                .collect(Collectors.toList());

        log.info("GET /schedule");
        log.info("Get info of all schedules");
        log.info(scheduleDTOs.toString());

        return scheduleDTOs;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<ScheduleDTO> scheduleDTOs = scheduleService.getSchedulesForAPet(petId)
                .stream()
                .map(scheduleModelMapperUtil::convertToDTO)
                .collect(Collectors.toList());

        log.info("GET /schedule/pet/{}",petId);
        log.info("Get info of schedules find by pet id: {}",petId);
        log.info(scheduleDTOs.toString());

        return scheduleDTOs;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<ScheduleDTO> scheduleDTOs = scheduleService.getSchedulesForAnEmployee(employeeId)
                .stream()
                .map(scheduleModelMapperUtil::convertToDTO)
                .collect(Collectors.toList());

        log.info("GET /schedule/employee/{}",employeeId);
        log.info("Get info of schedules find by employee id: {}",employeeId);
        log.info(scheduleDTOs.toString());

        return scheduleDTOs;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<ScheduleDTO> scheduleDTOs = scheduleService.getSchedulesForACustomer(customerId)
                .stream()
                .map(scheduleModelMapperUtil::convertToDTO)
                .collect(Collectors.toList());

        log.info("GET /schedule/customer/{}",customerId);
        log.info("Get info of schedules find by customer id: {}",customerId);
        log.info(scheduleDTOs.toString());

        return scheduleDTOs;
    }

}
