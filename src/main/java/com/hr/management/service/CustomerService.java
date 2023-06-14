package com.hr.management.service;

import com.hr.management.dto.mapper.CustomerRequestMapper;
import com.hr.management.dto.mapper.CustomerResponseMapper;
import com.hr.management.dto.request.CustomerRequest;
import com.hr.management.dto.response.CommandResponse;
import com.hr.management.dto.response.CustomerResponse;
import com.hr.management.model.Customer;
import com.hr.management.repository.AddressRepository;
import com.hr.management.repository.CustomerRepository;
import com.hr.management.common.Constants;
import com.hr.management.exception.ElementAlreadyExistsException;
import com.hr.management.exception.NoSuchElementFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service used for Customer related operations
 */
@Slf4j(topic = "CustomerService")
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerRequestMapper customerRequestMapper;
    private final CustomerResponseMapper customerResponseMapper;

    private final AddressRepository addressRepository;

    /**
     * Fetches a single customer by the given id
     *
     * @param id
     * @return CustomerResponse
     */
    public CustomerResponse findById(long id) {
        return customerRepository.findById(id)
                .map(customerResponseMapper::toDto)
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_CUSTOMER));
    }

    public List<CustomerResponse> findBySearchCriteria(Specification<Customer> spec, Pageable page) {
        List<CustomerResponse> searchResult = customerRepository.findAll(spec, page).stream().map(customerResponseMapper::toDto).toList();
        return searchResult;
    }

    /**
     * Fetches a single customer (entity) by the given id
     *
     * @param id
     * @return Customer
     */
    public Customer getById(long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_CUSTOMER));
    }

    /**
     * Fetches all customers based on the given paging and sorting parameters
     *
     * @param pageable
     * @return List of CustomerResponse
     */
    @Transactional(readOnly = true)
    public Page<CustomerResponse> findAll(Pageable pageable) {
        final Page<CustomerResponse> customers = customerRepository.findAll(pageable)
                .map(customerResponseMapper::toDto);

        if (customers.isEmpty())
            throw new NoSuchElementFoundException(Constants.NOT_FOUND_RECORD);
        return customers;
    }


    /**
     * Creates a new customer using the given request parameters
     *
     * @param request
     * @return id of the created customer
     */
    public CustomerResponse create(CustomerRequest request) {
        // Only creates a customer if it cannot find another customer with the same first and last names
        if (customerRepository.existsByFirstNameAndLastName(request.getFirstName(), request.getLastName()))
            throw new ElementAlreadyExistsException(Constants.ALREADY_EXISTS_CUSTOMER);

        final Customer customer = customerRequestMapper.toEntity(request);

        customerRepository.save(customer);
        log.info(Constants.CREATED_CUSTOMER);
        return customerResponseMapper.toDto(customer);
    }

    /**
     * Updates customer using the given request parameters
     *
     * @param request
     * @return id of the updated customer
     */
    public CommandResponse update(CustomerRequest request) {
        final Customer customer = customerRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_CUSTOMER));

        if ( customer.getAddresses() != null && !customer.getAddresses().isEmpty() ) {
            customer.setAddresses(customer.getAddresses().stream().map(m -> {
                m.setCustomer(customer);
                return m;
            }).collect(Collectors.toSet()));
        }

        customerRepository.save(customer);
        log.info(Constants.UPDATED_CUSTOMER);
        return CommandResponse.builder().id(customer.getId()).build();
    }

    /**
     * Deletes customer by the given id
     *
     * @param id
     */
    public void deleteById(long id) {
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_CUSTOMER));
        customerRepository.delete(customer);
        log.info(Constants.DELETED_CUSTOMER);
    }
}
