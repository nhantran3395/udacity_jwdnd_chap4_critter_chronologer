package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleModelMapperUtil scheduleModelMapperUtil;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleModelMapperUtil.convertToEntity(scheduleDTO);
        schedule.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return scheduleModelMapperUtil.convertToDTO(scheduleService.addSchedule(schedule));

    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.getAllSchedules()
                .stream()
                .map(scheduleModelMapperUtil::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return scheduleService.getSchedulesForAPet(petId)
                .stream()
                .map(scheduleModelMapperUtil::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return scheduleService.getSchedulesForAnEmployee(employeeId)
                .stream()
                .map(scheduleModelMapperUtil::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        return scheduleService.getSchedulesForACustomer(customerId)
                .stream()
                .map(scheduleModelMapperUtil::convertToDTO)
                .collect(Collectors.toList());
    }

}
