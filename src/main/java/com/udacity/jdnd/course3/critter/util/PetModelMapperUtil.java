package com.udacity.jdnd.course3.critter.util;

import com.udacity.jdnd.course3.critter.errorhandling.EntityNotFoundException;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.DTO.PetDTO;
import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PetModelMapperUtil {

    @Autowired
    private CustomerRepository customerRepository;

    private final ModelMapper modelMapper;

    public PetModelMapperUtil(){

        this.modelMapper = new ModelMapper();

        modelMapper.typeMap(Pet.class, PetDTO.class)
                .addMapping(pet -> pet.getCustomer().getId(),PetDTO::setOwnerId);

        final Converter<Long, Customer> ownerIdToCustomerEntityConverter = context ->
        {
            if(context.getSource() == null){
                return null;
            }

            return customerRepository.findById(context.getSource()).orElseThrow(() -> new EntityNotFoundException(Customer.class,"id",context.getSource().toString()));
        };

        modelMapper.typeMap(PetDTO.class, Pet.class)
                .addMappings(mapper -> mapper.using(ownerIdToCustomerEntityConverter).map(PetDTO::getOwnerId,Pet::setCustomer));
    }

    public PetDTO convertToDTO(Pet pet){

        if(Objects.isNull(pet)){
            return null;
        }

        return modelMapper.map(pet,PetDTO.class);
    }

    public Pet convertToEntity(PetDTO petDTO){

        if(Objects.isNull(petDTO)){
            return null;
        }

        return modelMapper.map(petDTO,Pet.class);
    }
}
