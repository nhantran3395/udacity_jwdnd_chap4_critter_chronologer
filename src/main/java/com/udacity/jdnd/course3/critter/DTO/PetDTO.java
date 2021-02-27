package com.udacity.jdnd.course3.critter.DTO;

import com.udacity.jdnd.course3.critter.model.model_enum.PetType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
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

    @NotNull
    private PetType type;

    @NotBlank
    private String name;

    @NotNull
    private Long ownerId;

    @NotNull
    @Past
    private LocalDate birthDate;

    private String notes;
}
