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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * might cause exception when username is duplicated
     */
    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        Customer customer = this.convertToEntity(customerDTO);
        customer.setCreateAt(new Timestamp(System.currentTimeMillis()));
        customer.setPets(new ArrayList<Pet>());

        Customer customerAdded = null;

        try{
            customerAdded = customerService.addCustomer(customer);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return this.convertToDTO(customerAdded);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.getAllCustomers()
                .stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer owner = customerService.getCustomerByPetId(petId);
        return this.convertToDTO(owner);
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

        if(Objects.isNull(customer)){
            return  null;
        }

        return modelMapper.map(customer,CustomerDTO.class);
    }

    private Customer convertToEntity(CustomerDTO customerDTO){

        if(Objects.isNull(customerDTO)){
            return  null;
        }

        return modelMapper.map(customerDTO,Customer.class);
    }
}
