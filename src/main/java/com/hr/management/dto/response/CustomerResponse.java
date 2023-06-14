package com.hr.management.dto.response;

import com.hr.management.dto.request.AddressRequest;
import com.hr.management.model.Address;
import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * Data Transfer Object for Customer response
 */
@Data
public class CustomerResponse {

    private Long id;

    private String firstName;

    private String lastName;


    private Integer age;

    private Date createdAt;

    private Set<AddressRequest> addresses;

}
