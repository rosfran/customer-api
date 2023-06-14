package com.hr.management.search;

import com.hr.management.model.Customer;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * That creates a list of related SearchCriterias, so that the Search for customers can be done in a parametrized way
 *
 * This implements a Builder design pattern to create Specifications for SearchCriteria
 *
 * Builder is a creational design pattern that lets you construct complex objects step by step.
 * The pattern allows you to produce different types and representations of an object using the same construction code.
 *
 */
public class CustomerSearchBuilder {

    private final List<SearchCriteria> params;

    public CustomerSearchBuilder(){
        this.params = new ArrayList<>();
    }

    public final CustomerSearchBuilder with(String key, String operation, Object value){
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public final CustomerSearchBuilder with(SearchCriteria searchCriteria){
        params.add(searchCriteria);
        return this;
    }

    public Specification<Customer> build(){
        if(params.size() == 0){
            return null;
        }

        Specification<Customer> result = new CustomerSearch(params.get(0));
        for (int idx = 1; idx < params.size(); idx++){
            final SearchCriteria criteria = params.get(idx);
            result = SearchOperation.getDataOption(criteria.getDataOption()) == SearchOperation.ALL
                    ? Specification.where(result).and(new CustomerSearch(criteria))
                    : Specification.where(result).or(new CustomerSearch(criteria));
        }

        return result;
    }
}
