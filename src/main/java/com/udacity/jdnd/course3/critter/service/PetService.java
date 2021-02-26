package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.errorhandling.EntityNotFoundException;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Pet addPet (Pet pet){
        Pet petSaved = petRepository.saveAndFlush(pet);

        if(petSaved.getCustomer() != null){
            if(petSaved.getCustomer().getPets() == null){
                petSaved.getCustomer().setPets(new ArrayList<>());
            }
            petSaved.getCustomer().getPets().add(pet);
        }

        return petSaved;
    }

    public List<Pet> getAllPets (){
        return petRepository.findAll();
    }

    public Pet getPetById (Long petId) {
        return petRepository.findById(petId).orElseThrow(()->{return new EntityNotFoundException(Pet.class,"id",petId.toString());});
    }

    public List<Pet> getPetsByOwner (Long ownerId) {
        if(!customerRepository.findById(ownerId).isPresent()){
            throw new EntityNotFoundException(Customer.class,"ownerId",ownerId.toString());
        }

        return petRepository.findByCustomer_id(ownerId);
    }

    public Pet updatePetOwner (Long ownerId, Long petId) throws Exception {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new EntityNotFoundException(Pet.class,"id",petId.toString()));
        Customer customer = customerRepository.findById(ownerId).orElseThrow(() -> new EntityNotFoundException(Customer.class,"ownerId",ownerId.toString()));

        pet.setCustomer(customer);

        return petRepository.save(pet);
    }

}
