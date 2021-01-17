package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import com.udacity.jdnd.course3.critter.exception.CustomerNotFoundException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        if (pet != null) {
            pet.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        PetDTO petDTOReturned = this.convertToDTO(petService.addPet(pet));

        log.info("POST /pet");
        log.info("Create a new pet");
        log.info(petDTO.toString());
        log.info(petDTOReturned != null ? petDTOReturned.toString() : null);

        return petDTOReturned;
    }

    @PutMapping("/{petId}")
    public PetDTO setOwner(@RequestBody Long ownerId, @PathVariable long petId) {
        Pet petUpdated = null;

        try{
            petUpdated = petService.updatePetOwner(ownerId, petId);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        PetDTO petDTOReturned = this.convertToDTO(petService.addPet(petUpdated));

        log.info("PUT /pet/{}",petId);
        log.info("Set owner to a pet: ownerId = {}",ownerId);
        log.info(petDTOReturned != null ? petDTOReturned.toString() : null);

        return this.convertToDTO(petUpdated);
    }


    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        PetDTO petDTOReturned = this.convertToDTO(petService.getPetById(petId));

        log.info("GET /pet/{}",petId);
        log.info("Get info of a pet");
        log.info(petDTOReturned != null ? petDTOReturned.toString() : null);

        return petDTOReturned;
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<PetDTO> petDTOS = petService.getAllPets()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        log.info("GET /pet");
        log.info("Get info of all pets");
        log.info(petDTOS.toString());

        return petDTOS;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<PetDTO> petDTOs = petService.getPetsByOwner(ownerId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        log.info("GET /pet/owner/{}",ownerId);
        log.info("Get info of a pet find by owner: ownerId = {}",ownerId);
        log.info(petDTOs.toString());

        return petDTOs;
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
