package com.udacity.jdnd.course3.critter.pet;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = this.convertToEntity(petDTO);
        pet.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return this.convertToDTO(petService.addPet(pet));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return this.convertToDTO(petService.getPetById(petId));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        return petService.getAllPets()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        return petService.getPetsByOwner(ownerId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostConstruct
    private void postConstruct(){
        modelMapper.typeMap(Pet.class, PetDTO.class)
                .addMapping(pet -> pet.getCustomer().getId(),PetDTO::setOwnerId);
    }

    private PetDTO convertToDTO(Pet pet){

        if(Objects.isNull(pet)){
            return null;
        }

        return modelMapper.map(pet,PetDTO.class);
    }

    private Pet convertToEntity(PetDTO petDTO){

        if(Objects.isNull(petDTO)){
            return null;
        }

        return modelMapper.map(petDTO,Pet.class);
    }
}
