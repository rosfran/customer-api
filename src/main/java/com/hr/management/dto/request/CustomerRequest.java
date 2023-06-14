package com.hr.management.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Set;

/**
 * Data Transfer Object for Customer request
 */
@Data
@Builder
@EqualsAndHashCode(of = {"firstName", "lastName"})
public class CustomerRequest {

    private Long id;

    private String firstName;

    private String lastName;


    private Integer age;

    private Date createdAt;

    private Set<AddressRequest> addresses;

}
