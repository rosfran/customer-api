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
@EqualsAndHashCode(of = {"streetName", "city", "number"})
public class AddressRequest {

    private Long id;

    private String streetName;

    private String city;

    private String country;

    private Integer number;

    private Date createdAt;

}
