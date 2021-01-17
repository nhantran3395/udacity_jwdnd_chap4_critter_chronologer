package com.udacity.jdnd.course3.critter.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Represents the form that customer request and response data takes. Does not map
 * to the database directly.
 */
@Getter
@Setter
@ToString
public class CustomerDTO {
    private long id;
    private String username;
    private String name;
    private String phoneNumber;
    private String notes;
    private List<Long> petIds;
}
