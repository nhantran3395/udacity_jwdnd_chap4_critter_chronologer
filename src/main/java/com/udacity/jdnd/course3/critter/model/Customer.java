package com.udacity.jdnd.course3.critter.model;

import com.udacity.jdnd.course3.critter.model.Pet;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String name;

    @NotNull
    @Column(name="phone_number")
    private String phoneNumber;

    private String notes;

    @CreatedDate
    @Column(name="created_at",nullable = false,columnDefinition = "datetime2 default getdate()")
    private Timestamp createAt;

    @LastModifiedDate
    @Column(name="updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "customer",fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST)
    private List<Pet> pets;
}
