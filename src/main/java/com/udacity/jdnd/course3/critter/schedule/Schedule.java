package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.List;


@Getter
@Setter
@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "schedules", fetch = FetchType.LAZY)
    private List<Pet> pets;

    private LocalDate date;

    @CreatedDate
    @Column(name = "created_at", nullable = false, columnDefinition = "datetime2 default getdate()")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToMany(mappedBy = "schedules", fetch = FetchType.LAZY)
    private List<Employee> employees;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "ScheduleActivity",
            joinColumns = {
                    @JoinColumn(name = "schedule_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "activity_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})
    private Set<Activity> activities = new HashSet<>();
}
