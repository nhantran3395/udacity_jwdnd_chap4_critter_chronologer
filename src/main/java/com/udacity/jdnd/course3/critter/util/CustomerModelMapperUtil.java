package com.udacity.jdnd.course3.critter.util;

import com.udacity.jdnd.course3.critter.exception.PetNotFoundException;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CustomerModelMapperUtil {

    private final ModelMapper modelMapper;

    @Autowired
    private PetRepository petRepository;

    public CustomerModelMapperUtil(){

        this.modelMapper = new ModelMapper();

        final Converter<List<Pet>,List<Long>> petEntityToPetIdConverter = context -> {
            if(context.getSource() == null){
                return null;
            }
            return context.getSource()
                    .stream()
                    .map(Pet::getId)
                    .collect(Collectors.toList());
        };

        final Converter<List<Long>,List<Pet>> petIdToPetEntityConverter = context -> {
            if(context.getSource() == null){
                return null;
            }
            return context.getSource()
                    .stream()
                    .map(petId -> petRepository.findById(petId).orElseThrow(() -> new PetNotFoundException("no such pet")))
                    .collect(Collectors.toList());
        };


        modelMapper.typeMap(Customer.class, CustomerDTO.class)
                .addMappings(mapper -> mapper.using(petEntityToPetIdConverter).map(Customer::getPets,CustomerDTO::setPetIds));

        modelMapper.typeMap(CustomerDTO.class,Customer.class)
                .addMappings(mapper -> mapper.using(petIdToPetEntityConverter).map(CustomerDTO::getPetIds,Customer::setPets));
    }

    public CustomerDTO convertToCustomerDTO(Customer customer){

        if(Objects.isNull(customer)){
            return  null;
        }

        return modelMapper.map(customer,CustomerDTO.class);
    }

    public Customer convertToCustomerEntity(CustomerDTO customerDTO){

        if(Objects.isNull(customerDTO)){
            return  null;
        }

        return modelMapper.map(customerDTO,Customer.class);
    }
}
