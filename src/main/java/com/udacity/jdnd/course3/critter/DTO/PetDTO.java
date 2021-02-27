package com.udacity.jdnd.course3.critter.DTO;

import com.udacity.jdnd.course3.critter.model.model_enum.PetType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * Represents the form that pet request and response data takes. Does not map
 * to the database directly.
 */
@Getter
@Setter
@ToString
public class PetDTO {
    private Long id;

    @NotBlank
    private PetType type;

    @NotBlank
    private String name;

    @NotBlank
    private Long ownerId;

    @NotBlank
    private LocalDate birthDate;
    private String notes;
}
