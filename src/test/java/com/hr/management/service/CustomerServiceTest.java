package com.hr.management.service;

import com.hr.management.dto.mapper.CustomerRequestMapper;
import com.hr.management.dto.mapper.CustomerResponseMapper;
import com.hr.management.dto.request.AddressRequest;
import com.hr.management.dto.request.CustomerRequest;
import com.hr.management.dto.response.CommandResponse;
import com.hr.management.model.Customer;
import com.hr.management.repository.CustomerRepository;
import com.hr.management.exception.ElementAlreadyExistsException;
import com.hr.management.exception.NoSuchElementFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test for CustomerService methods
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerRequestMapper customerRequestMapper;

    @Mock
    private CustomerResponseMapper customerResponseMapper;

    @Captor
    private ArgumentCaptor<Customer> recipeCaptor;

    /**
     * Method under test: {@link CustomerService#getById(long)}
     */
    @Test
    void getById_should_throwNoSuchElementFoundException_when_IsNotFound() {
        long id = 101L;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            customerService.getById(id);
        });

        verify(customerRepository).findById(id);
    }


    /**
     * Method under test: {@link CustomerService#findAll(Pageable)}
     */
    @Test
    void findAll_should_throwNoSuchElementFoundException_when_IsNotFound() {
        Pageable pageable = PageRequest.of(0, 10);
        when(customerRepository.findAll(pageable)).thenReturn(new PageImpl<>(new ArrayList<>()));

        assertThrows(NoSuchElementFoundException.class, () -> {
            customerService.findAll(pageable);
        });

        verify(customerRepository).findAll(pageable);
    }


    /**
     * Method under test: {@link CustomerService#create(CustomerRequest)}
     */
    @Test
    void create_should_throwElementAlreadyExistsException_when_RecipeAlreadyExists() {
        CustomerRequest request = CustomerRequest.builder()
                .firstName("James")
                .lastName("Munt")
                .addresses( Set.of(
                    AddressRequest.builder().streetName("Top").city("Amstelveen").build(),
                    AddressRequest.builder().streetName("Postjesweg").city("Amsterdam").build() )
                )
                .build();

        when(customerRepository.existsByFirstNameAndLastName("James", "Munt")).thenReturn(true);

        assertThrows(ElementAlreadyExistsException.class, () -> {
            customerService.create(request);
        });

        verify(customerRepository, never()).save(any());
    }


    /**
     * Method under test: {@link CustomerService#update(CustomerRequest)}
     */
    @Test
    void update_should_throwElementAlreadyExistsException_when_RecipeAlreadyExists() {
        Customer customer = Customer.builder()
                .id(101L)
                .firstName("Gepeto")
                .lastName("voor Pinocchio")
                .age(120).build();

        when(customerRepository.findById(101L)).thenReturn(Optional.of(customer));

        final CustomerRequest customerRequest = customerRequestMapper.toDto(customer);

        CustomerRequest r = CustomerRequest.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .age(customer.getAge()).build();

        assertNotNull(r);
        assertEquals(101l, r.getId());

        CommandResponse resp = customerService.update(r);

        assertEquals( 101l, resp.id());

    }

    /**
     * Method under test: {@link CustomerService#deleteById(long)}
     */
    @Test
    void deleteById_should_deleteRecipes_when_IsFound() {
        Customer customer =  Customer.builder().id(101L).firstName("Gepeto").build();

        when(customerRepository.findById(101L)).thenReturn(Optional.of(customer));

        customerService.deleteById(101L);
        verify(customerRepository).delete(recipeCaptor.capture());
        Customer capturedCustomer = recipeCaptor.getValue();

        assertEquals(101L, capturedCustomer.getId());
        assertEquals("Gepeto", capturedCustomer.getFirstName());
    }

    /**
     * Method under test: {@link CustomerService#deleteById(long)}
     */
    @Test
    void deleteById_should_throwNoSuchElementFoundException_when_RecipesIsNotFound() {
        long id = 101L;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            customerService.deleteById(id);
        });

        verify(customerRepository).findById(id);
        verify(customerRepository, never()).delete(new Customer());
    }
}
