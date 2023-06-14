package com.hr.management.repository;

import com.hr.management.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    List<Customer> findAllByFirstName(String firstName);

}
