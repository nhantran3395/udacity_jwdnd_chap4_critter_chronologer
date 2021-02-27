package com.udacity.jdnd.course3.critter.model;

import com.udacity.jdnd.course3.critter.model.model_enum.PetType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private PetType type;

    @NotBlank
    private LocalDate birthDate;

    private String notes;

    @CreatedDate
    @Column(name = "created_at", nullable = false, columnDefinition = "datetime2 default getdate()")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id",nullable = false)
    private Customer customer;

    @ManyToMany(mappedBy = "pets",fetch = FetchType.LAZY)
    private Set<Schedule> schedules;
}
