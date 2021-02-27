package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.errorhandling.EntityNotFoundException;
import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;

    public Customer addCustomer (Customer customer) throws ValidationException {
        if(customerRepository.findByUsername(customer.getUsername()).isPresent()){
            throw new ValidationException("username exist!");
        }

        Customer customerSaved = customerRepository.saveAndFlush(customer);

        if (customerSaved.getPets() != null) {
            customerSaved.getPets().forEach(pet -> pet.setCustomer(customerSaved));
        }

        return customerSaved;
    }

    public List<Customer> getAllCustomers () {
        return customerRepository.findAll();
    }

    public Customer getCustomerById (Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(()->{return new EntityNotFoundException(Employee.class,"id",customerId.toString());});
    }

    public Customer getCustomerByPetId (Long petId) {
        if(!petRepository.findById(petId).isPresent()){
            throw new EntityNotFoundException(Pet.class,"id",petId.toString());
        }

        return customerRepository.findByPets_id(petId).orElseThrow(()->{return new EntityNotFoundException(Customer.class,"petId",petId.toString());});
    }
}
