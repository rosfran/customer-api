package com.hr.management.dto.mapper;

import com.hr.management.dto.request.CustomerRequest;
import com.hr.management.model.Address;
import com.hr.management.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper used for mapping CustomerRequest fields
 */
@Mapper(componentModel = "spring")
public interface CustomerRequestMapper {

    @Mapping(target = "firstName", expression = "java(org.apache.commons.text.WordUtils.capitalizeFully(dto.getFirstName()))")
    Customer toEntity(CustomerRequest dto);

    default Address map(String value ) {
        return new Address(value);
    }

    default String map(Address address) {
        return address.getStreetName();
    }

    CustomerRequest toDto(Customer entity);
}
