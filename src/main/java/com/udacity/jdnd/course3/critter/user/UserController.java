package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import exception.CustomerException;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        Customer customer = this.convertToEntity(customerDTO);
        customer.setCreateAt(new Timestamp(System.currentTimeMillis()));
        customer.setPets(new ArrayList<Pet>());

        try{
            return this.convertToDTO(customerService.addCustomer(customer));
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.getAllCustomers()
                .stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        throw new UnsupportedOperationException();
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        throw new UnsupportedOperationException();
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        throw new UnsupportedOperationException();
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        throw new UnsupportedOperationException();
    }

    @PostConstruct
    private void postConstruct(){
        final Converter<List<Pet>,List<Long>> petListConverter = context ->
                context.getSource()
                        .stream()
                        .map(Pet::getId)
                        .collect(Collectors.toList());

        modelMapper.typeMap(Customer.class,CustomerDTO.class)
                .addMappings(mapper -> mapper.using(petListConverter).map(Customer::getPets,CustomerDTO::setPetIds));
    }

    private CustomerDTO convertToDTO(Customer customer){
        CustomerDTO customerDTO = modelMapper.map(customer,CustomerDTO.class);
        return customerDTO;
    }

    private Customer convertToEntity(CustomerDTO customerDTO){
        Customer customer = modelMapper.map(customerDTO,Customer.class);
        return customer;
    }
}
