package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AvailableDayRepository availableDayRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * might cause com.udacity.jdnd.course3.critter.exception when username is duplicated
     */
    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        Customer customer = this.convertToCustomerEntity(customerDTO);
        customer.setCreateAt(new Timestamp(System.currentTimeMillis()));
        customer.setPets(new ArrayList<Pet>());

        Customer customerAdded = null;

        try{
            customerAdded = customerService.addCustomer(customer);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return this.convertToCustomerDTO(customerAdded);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.getAllCustomers()
                .stream().map(this::convertToCustomerDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer owner = customerService.getCustomerByPetId(petId);
        return this.convertToCustomerDTO(owner);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = this.convertToEmployeeEntity(employeeDTO);
        employee.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        Employee employeeAdded = null;

        try{
            employeeAdded = employeeService.addEmployee(employee);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return this.convertToEmployeeDTO(employeeAdded);
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return this.convertToEmployeeDTO(employeeService.getEmployeeByID(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<java.time.DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Set <AvailableDay> availableDays = convertDayOfWeekEnumToAvailableDayEntity(daysAvailable);
        Employee employeeUpdated = null;

        try{
            employeeUpdated = employeeService.updateEmployeeAvailability(availableDays,employeeId);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeRequestDTO) {
        LocalDate date = employeeRequestDTO.getDate();
        Set<SkillEnum> employeeSkills = employeeRequestDTO.getSkills();

        return employeeService.getEmployeesMatchedRequest(date, employeeSkills).stream()
                .map(this::convertToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @PostConstruct
    private void postConstruct(){
        final Converter<List<Pet>,List<Long>> petEntityToPetIdConverter = context -> {
            if(context.getSource() == null){
                return null;
            }
            return context.getSource()
                    .stream()
                    .map(Pet::getId)
                    .collect(Collectors.toList());
        };

        modelMapper.typeMap(Customer.class,CustomerDTO.class)
                .addMappings(mapper -> mapper.using(petEntityToPetIdConverter).map(Customer::getPets,CustomerDTO::setPetIds));

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
                    .map(skill -> skillRepository.findBySkill(skill).orElseGet(null))
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

        final Converter<Set<DayOfWeek>,Set<AvailableDay>> dayOfWeekToAvailalbeDayEntityConverter= context -> {
            if(context.getSource() == null){
                return null;
            }

            return context.getSource()
                    .stream()
                    .map(day->availableDayRepository.findByDay(day).orElseGet(null))
                    .collect(Collectors.toSet());
        };

        modelMapper.typeMap(Employee.class,EmployeeDTO.class)
                .addMappings(mapper -> mapper.using(skillEntityToSkillEnumConverter).map(Employee::getSkills,EmployeeDTO::setSkills))
                .addMappings(mapper -> mapper.using(availableDayEntityToDayOfWeekEnumConverter).map(Employee::getAvailableDays,EmployeeDTO::setDaysAvailable));

        modelMapper.typeMap(EmployeeDTO.class,Employee.class)
                .addMappings(mapper -> mapper.using(skillEnumToSkillEntityConverter).map(EmployeeDTO::getSkills,Employee::setSkills))
                .addMappings(mapper -> mapper.using(dayOfWeekToAvailalbeDayEntityConverter).map(EmployeeDTO::getDaysAvailable,Employee::setAvailableDays));
    }

    private CustomerDTO convertToCustomerDTO(Customer customer){

        if(Objects.isNull(customer)){
            return  null;
        }

        return modelMapper.map(customer,CustomerDTO.class);
    }

    private Customer convertToCustomerEntity(CustomerDTO customerDTO){

        if(Objects.isNull(customerDTO)){
            return  null;
        }

        return modelMapper.map(customerDTO,Customer.class);
    }

    private EmployeeDTO convertToEmployeeDTO(Employee employee){

        if(Objects.isNull(employee)){
            return  null;
        }

        return modelMapper.map(employee,EmployeeDTO.class);
    }

    private Employee convertToEmployeeEntity(EmployeeDTO employeeDTO){

        if(Objects.isNull(employeeDTO)){
            return  null;
        }

        return modelMapper.map(employeeDTO,Employee.class);
    }

    private Set<AvailableDay> convertDayOfWeekEnumToAvailableDayEntity (Set <DayOfWeek> days){
        return days.stream()
                .map(day->availableDayRepository.findByDay(day).orElseGet(null))
                .collect(Collectors.toSet());
    }

    private Set<Skill> convertSkillEnumToSkillEntity (Set <SkillEnum> employeeSkills){
        return employeeSkills.stream()
                .map(employeeSkill -> skillRepository.findBySkill(employeeSkill).orElseGet(null))
                .collect(Collectors.toSet());
    }

}
