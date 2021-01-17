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

        log.info("POST user/customer");
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

        log.info("GET user/customer");
        log.info("Get info of all customers");
        log.info(customerDTOs.toString());

        return customerDTOs;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer owner = customerService.getCustomerByPetId(petId);
        CustomerDTO ownerDTO = customerModelMapperUtil.convertToCustomerDTO(owner);

        log.info("GET user/customer/pet/{}",petId);
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
        
        return employeeModelMapperUtil.convertToEmployeeDTO(employeeAdded);
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return employeeModelMapperUtil.convertToEmployeeDTO(employeeService.getEmployeeByID(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<java.time.DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Set <AvailableDay> availableDays = employeeModelMapperUtil.convertDayOfWeekEnumToAvailableDayEntity(daysAvailable);
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
                .map(employeeModelMapperUtil::convertToEmployeeDTO)
                .collect(Collectors.toList());
    }
    
}
