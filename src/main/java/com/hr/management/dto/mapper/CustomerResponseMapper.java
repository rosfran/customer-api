package com.hr.management.dto.mapper;

import com.hr.management.dto.response.CustomerResponse;
import com.hr.management.model.Address;
import com.hr.management.model.Customer;
import org.mapstruct.Mapper;

/**
 * Mapper used for mapping CustomerResponse fields
 */
@Mapper(componentModel = "spring")
public interface CustomerResponseMapper {

    Customer toEntity(CustomerResponse dto);

    default Address map(String value ) {
        return new Address(value);
    }

    default String map(Address address) {
        return address.getStreetName();
    }

    CustomerResponse toDto(Customer entity);
}
