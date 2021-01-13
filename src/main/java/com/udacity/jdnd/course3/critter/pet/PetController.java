package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import com.udacity.jdnd.course3.critter.exception.CustomerNotFoundException;
import org.modelmapper.Converter;
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
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = this.convertToEntity(petDTO);
        pet.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return this.convertToDTO(petService.addPet(pet));
    }

    @PutMapping("/{petId}")
    public void setOwner(@RequestBody Long ownerId, @PathVariable long petId) {
        Pet petUpdated = null;

        try{
            petUpdated = petService.updatePetOwner(ownerId, petId);
        }
        catch(Exception e){
            e.printStackTrace();
        }
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

        final Converter<Long, Customer> ownerIdToCustomerEntityConverter = context ->
        {
            if(context.getSource() == null){
                return null;
            }

            return customerRepository.findById(context.getSource()).orElseThrow(() -> new CustomerNotFoundException("no such customer"));
        };

        modelMapper.typeMap(PetDTO.class, Pet.class)
                .addMappings(mapper -> mapper.using(ownerIdToCustomerEntityConverter).map(PetDTO::getOwnerId,Pet::setCustomer));
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
