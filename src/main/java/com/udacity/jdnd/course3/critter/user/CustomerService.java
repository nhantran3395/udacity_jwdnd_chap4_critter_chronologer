package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.exception.CustomerInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer addCustomer (Customer customer) throws CustomerInvalidException {
        customerRepository.findByUsername(customer.getUsername())
                .ifPresent(c->{throw new CustomerInvalidException("username is duplicated");});

        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers () {
        return customerRepository.findAll();
    }

    public Customer getCustomerByPetId (Long petId) {
        return customerRepository.findByPets_id(petId).orElse(null);
    }
}
