package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.DTO.PetDTO;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.util.PetModelMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;
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
    private PetModelMapperUtil petModelMapperUtil;

    @PostMapping
    public ResponseEntity<PetDTO> savePet(@RequestBody @Valid PetDTO petDTO) {

        Pet pet = petModelMapperUtil.convertToEntity(petDTO);
        if (pet != null) {
            pet.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        PetDTO petDTOReturned = petModelMapperUtil.convertToDTO(petService.addPet(pet));

        log.info("POST /pet");
        log.info("Create a new pet");
        log.info(petDTO.toString());
        log.info(petDTOReturned != null ? petDTOReturned.toString() : null);

        return new ResponseEntity<PetDTO> (petDTOReturned,HttpStatus.CREATED);
    }

    @PutMapping("/{petId}")
    public ResponseEntity<PetDTO> setOwner(@RequestBody Long ownerId, @PathVariable Long petId) throws Exception {
        Pet petUpdated = petService.updatePetOwner(ownerId, petId);

        PetDTO petDTOReturned = petModelMapperUtil.convertToDTO(petService.addPet(petUpdated));

        log.info("PUT /pet/{}",petId);
        log.info("Set owner to a pet: ownerId = {}",ownerId);
        log.info(petDTOReturned != null ? petDTOReturned.toString() : null);

        return new ResponseEntity<PetDTO> (petDTOReturned,HttpStatus.OK);
    }


    @GetMapping("/{petId}")
    public ResponseEntity<PetDTO> getPet(@PathVariable long petId) {
        PetDTO petDTOReturned = petModelMapperUtil.convertToDTO(petService.getPetById(petId));

        log.info("GET /pet/{}",petId);
        log.info("Get info of a pet");
        log.info(petDTOReturned != null ? petDTOReturned.toString() : null);

        return new ResponseEntity<PetDTO> (petDTOReturned,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PetDTO>> getPets(){
        List<PetDTO> petDTOs = petService.getAllPets()
                .stream()
                .map(petModelMapperUtil::convertToDTO)
                .collect(Collectors.toList());

        log.info("GET /pet");
        log.info("Get info of all pets");
        log.info(petDTOs.toString());

        return new ResponseEntity<List<PetDTO>> (petDTOs,HttpStatus.OK);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<PetDTO>> getPetsByOwner(@PathVariable long ownerId) {
        List<PetDTO> petDTOs = petService.getPetsByOwner(ownerId)
                .stream()
                .map(petModelMapperUtil::convertToDTO)
                .collect(Collectors.toList());

        log.info("GET /pet/owner/{}",ownerId);
        log.info("Get info of a pet find by owner: ownerId = {}",ownerId);
        log.info(petDTOs.toString());

        return new ResponseEntity<List<PetDTO>> (petDTOs,HttpStatus.OK);
    }

}
