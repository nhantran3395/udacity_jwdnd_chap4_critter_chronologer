package com.udacity.jdnd.course3.critter.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String name;

    @CreatedDate
    @Column(name = "created_at", nullable = false, columnDefinition = "datetime2 default getdate()")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinTable(name = "EmployeeSkill",
            joinColumns = {
                    @JoinColumn(name = "employee_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "skill_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})
    private Set<Skill> skills;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinTable(name = "EmployeeAvailableDay",
            joinColumns = {
                    @JoinColumn(name = "employee_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "available_day_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})
    private Set<AvailableDay> availableDays;

    @ManyToMany(mappedBy = "employees",fetch = FetchType.LAZY)
    private Set<Schedule> schedules;

    public Employee(String username, String name, Set<AvailableDay> availableDays) {
        this.username = username;
        this.name = name;
        this.availableDays = availableDays;
    }
}
