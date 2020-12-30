package com.udacity.jdnd.course3.critter.user;

import exception.CustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer addCustomer (Customer customer) throws CustomerException {
        customerRepository.findByUsername(customer.getUsername())
                .ifPresent(c->{throw new CustomerException("username is duplicated");});

        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers () {
        return customerRepository.findAll();
    }

}
