package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

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
            cascade = CascadeType.ALL)
    private List<Pet> pets;

    @OneToMany(mappedBy = "customer",fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Schedule> schedules;
}
