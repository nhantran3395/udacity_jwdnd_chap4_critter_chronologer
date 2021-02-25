package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.exception.CustomerInvalidException;
import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer addCustomer (Customer customer) throws CustomerInvalidException {
        customerRepository.findByUsername(customer.getUsername())
                .ifPresent(c->{throw new CustomerInvalidException("username is duplicated");});

        Customer customerSaved = customerRepository.saveAndFlush(customer);

        if (customerSaved.getPets() != null) {
            customerSaved.getPets().forEach(pet -> pet.setCustomer(customerSaved));
        }

        return customerSaved;
    }

    public List<Customer> getAllCustomers () {
        return customerRepository.findAll();
    }

    public Customer getCustomerByPetId (Long petId) {
        return customerRepository.findByPets_id(petId).orElse(null);
    }
}
