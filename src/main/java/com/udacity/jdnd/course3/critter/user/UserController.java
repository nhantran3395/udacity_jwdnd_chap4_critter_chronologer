package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.util.CustomerModelMapperUtil;
import com.udacity.jdnd.course3.critter.util.EmployeeModelMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CustomerModelMapperUtil customerModelMapperUtil;

    @Autowired
    private EmployeeModelMapperUtil employeeModelMapperUtil;
    
    /**
     * might cause com.udacity.jdnd.course3.critter.exception when username is duplicated
     */
    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        Customer customer = customerModelMapperUtil.convertToCustomerEntity(customerDTO);
        customer.setCreateAt(new Timestamp(System.currentTimeMillis()));

        Customer customerAdded = null;

        try{
            customerAdded = customerService.addCustomer(customer);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        CustomerDTO customerDTOReturned = customerModelMapperUtil.convertToCustomerDTO(customerAdded);

        log.info("POST /user/customer");
        log.info("Create a new customer");
        log.info(customerDTO.toString());
        log.info(customerDTOReturned != null ? customerDTOReturned.toString() : null);

        return customerDTOReturned;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<CustomerDTO> customerDTOs = customerService.getAllCustomers()
                .stream().map(customerModelMapperUtil::convertToCustomerDTO)
                .collect(Collectors.toList());

        log.info("GET /user/customer");
        log.info("Get info of all customers");
        log.info(customerDTOs.toString());

        return customerDTOs;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer owner = customerService.getCustomerByPetId(petId);
        CustomerDTO ownerDTO = customerModelMapperUtil.convertToCustomerDTO(owner);

        log.info("GET /user/customer/pet/{}",petId);
        log.info("Get info of customer find by pet id");
        log.info(ownerDTO.toString());

        return ownerDTO;
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeModelMapperUtil.convertToEmployeeEntity(employeeDTO);
        employee.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        Employee employeeAdded = null;

        try{
            employeeAdded = employeeService.addEmployee(employee);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        EmployeeDTO employeeDTOReturned = employeeModelMapperUtil.convertToEmployeeDTO(employeeAdded);

        log.info("POST /user/employee");
        log.info("Create a new employee");
        log.info(employeeDTO.toString());
        log.info(employeeDTOReturned != null ? employeeDTOReturned.toString() : null);

        return employeeDTOReturned;
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        EmployeeDTO employeeDTOReturned = employeeModelMapperUtil.convertToEmployeeDTO(employeeService.getEmployeeByID(employeeId));

        log.info("GET /user/employee/{}",employeeId);
        log.info("Get info of an employee");
        log.info(employeeDTOReturned.toString());

        return employeeDTOReturned;
    }

    @PutMapping("/employee/{employeeId}")
    public EmployeeDTO setAvailability(@RequestBody Set<java.time.DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Set <AvailableDay> availableDays = employeeModelMapperUtil.convertDayOfWeekEnumToAvailableDayEntity(daysAvailable);
        Employee employeeUpdated = null;

        try{
            employeeUpdated = employeeService.updateEmployeeAvailability(availableDays,employeeId);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        EmployeeDTO employeeDTOReturned = employeeModelMapperUtil.convertToEmployeeDTO(employeeUpdated);

        log.info("PUT /user/employee/{}",employeeId);
        log.info("Set available day of an employees: daysAvailable = {}",daysAvailable);
        log.info(employeeDTOReturned != null ? employeeDTOReturned.toString() : null);

        return employeeDTOReturned;
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeRequestDTO) {
        LocalDate date = employeeRequestDTO.getDate();
        Set<SkillEnum> employeeSkills = employeeRequestDTO.getSkills();

        List<EmployeeDTO> employeeDTOs = employeeService.getEmployeesMatchedRequest(date, employeeSkills).stream()
                .map(employeeModelMapperUtil::convertToEmployeeDTO)
                .collect(Collectors.toList());

        log.info("GET /user/employee/availability");
        log.info("Get info of employees find by available day and skills");
        log.info("day: {}",date);
        log.info("skills: {}",employeeSkills);
        log.info(employeeDTOs.toString());

        return employeeDTOs;
    }
    
}
