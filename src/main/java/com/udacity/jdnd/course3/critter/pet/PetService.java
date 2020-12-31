package com.udacity.jdnd.course3.critter.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public Pet addPet (Pet pet){
        return petRepository.save(pet);
    }

    public List<Pet> getAllPets (){
        return petRepository.findAll();
    }

    public Pet getPetById (Long id) {
        return petRepository.findById(id).orElse(null);
    }

    public List<Pet> getPetsByOwner (Long ownerId) {
        return petRepository.findByCustomer_id(ownerId);
    }
}
