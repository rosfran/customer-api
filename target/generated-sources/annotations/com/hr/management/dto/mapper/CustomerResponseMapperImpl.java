package com.hr.management.dto.mapper;

import com.hr.management.dto.request.AddressRequest;
import com.hr.management.dto.response.CustomerResponse;
import com.hr.management.model.Address;
import com.hr.management.model.Customer;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-14T18:45:30+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17 (Homebrew)"
)
@Component
public class CustomerResponseMapperImpl implements CustomerResponseMapper {

    @Override
    public Customer toEntity(CustomerResponse dto) {
        if ( dto == null ) {
            return null;
        }

        Customer.CustomerBuilder customer = Customer.builder();

        customer.id( dto.getId() );
        customer.firstName( dto.getFirstName() );
        customer.lastName( dto.getLastName() );
        customer.age( dto.getAge() );
        customer.createdAt( dto.getCreatedAt() );
        customer.addresses( addressRequestSetToAddressSet( dto.getAddresses() ) );

        return customer.build();
    }

    @Override
    public CustomerResponse toDto(Customer entity) {
        if ( entity == null ) {
            return null;
        }

        CustomerResponse customerResponse = new CustomerResponse();

        customerResponse.setId( entity.getId() );
        customerResponse.setFirstName( entity.getFirstName() );
        customerResponse.setLastName( entity.getLastName() );
        customerResponse.setAge( entity.getAge() );
        customerResponse.setCreatedAt( entity.getCreatedAt() );
        customerResponse.setAddresses( addressSetToAddressRequestSet( entity.getAddresses() ) );

        return customerResponse;
    }

    protected Address addressRequestToAddress(AddressRequest addressRequest) {
        if ( addressRequest == null ) {
            return null;
        }

        Address address = new Address();

        address.setId( addressRequest.getId() );
        address.setStreetName( addressRequest.getStreetName() );
        address.setCity( addressRequest.getCity() );
        address.setCountry( addressRequest.getCountry() );
        address.setNumber( addressRequest.getNumber() );

        return address;
    }

    protected Set<Address> addressRequestSetToAddressSet(Set<AddressRequest> set) {
        if ( set == null ) {
            return null;
        }

        Set<Address> set1 = new LinkedHashSet<Address>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( AddressRequest addressRequest : set ) {
            set1.add( addressRequestToAddress( addressRequest ) );
        }

        return set1;
    }

    protected AddressRequest addressToAddressRequest(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressRequest.AddressRequestBuilder addressRequest = AddressRequest.builder();

        addressRequest.id( address.getId() );
        addressRequest.streetName( address.getStreetName() );
        addressRequest.city( address.getCity() );
        addressRequest.country( address.getCountry() );
        addressRequest.number( address.getNumber() );

        return addressRequest.build();
    }

    protected Set<AddressRequest> addressSetToAddressRequestSet(Set<Address> set) {
        if ( set == null ) {
            return null;
        }

        Set<AddressRequest> set1 = new LinkedHashSet<AddressRequest>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Address address : set ) {
            set1.add( addressToAddressRequest( address ) );
        }

        return set1;
    }
}
