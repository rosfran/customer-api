package com.hr.management.search;


import com.hr.management.model.Address;
import com.hr.management.model.Customer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;
import java.util.Objects;

/**
 * This is the parametrized Specification for our search criteria. It works creating the Predicates that will
 * make the SQL query to gather data from the Database
 *
 * Here we must define the ways to handle matching fields with the referenced tables, like Address.
 *
 * This is a very powerful and flexible mechanism, where we can use some different ways of filtering out data
 * using constructs like notLike, notContains, Contains, etc. *
 */
@Slf4j(topic = "CustomerSearch")
public class CustomerSearch implements Specification<Customer> {

    private final SearchCriteria searchCriteria;

    public CustomerSearch(final SearchCriteria searchCriteria){
        super();
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        // lowercase the Value from SearchCriteria, this is useful to some operations like CONTAINS
        String strToSearch = searchCriteria.getValue().toString().toLowerCase();

        switch(Objects.requireNonNull(SearchOperation.getSimpleOperation(searchCriteria.getFilter()))){
            case CONTAINS:
                if(searchCriteria.getKey().equals("address") )
                {
                    log.info("comparing CONTAINS origin {}", addressesJoinOrigin(root).<String>get("streetName"));
                    return cb.or(
                            cb.like(cb.lower(addressesJoinOrigin(root).<String>get("streetName")), "%" + strToSearch + "%"),
                            cb.like(cb.lower(addressesJoinOrigin(root).<String>get("city")), "%" + strToSearch + "%"),
                            cb.like(cb.lower(addressesJoinOrigin(root).<String>get("country")), "%" + strToSearch + "%")
                            );
                }
                return cb.like(cb.lower(root.get(searchCriteria.getKey())), "%" + strToSearch + "%");

            case DOES_NOT_CONTAIN:
                if(searchCriteria.getKey().equals("address") )
                {
                    log.info("comparing CONTAINS origin {}", addressesJoinOrigin(root).<String>get("streetName"));
                    return cb.or(
                            cb.notLike(cb.lower(addressesJoinOrigin(root).<String>get("streetName")), "%" + strToSearch + "%"),
                            cb.notLike(cb.lower(addressesJoinOrigin(root).<String>get("city")), "%" + strToSearch + "%"),
                            cb.notLike(cb.lower(addressesJoinOrigin(root).<String>get("country")), "%" + strToSearch + "%")
                    );
                }
                return cb.notLike(cb.lower(root.get(searchCriteria.getKey())), "%" + strToSearch + "%");
            case STARTS_WITH:
                if(searchCriteria.getKey().equals("address") )
                {
                    log.info("comparing CONTAINS origin {}", addressesJoinOrigin(root).<String>get("streetName"));
                    return cb.or(
                            cb.like(cb.lower(addressesJoinOrigin(root).<String>get("streetName")), strToSearch + "%"),
                            cb.like(cb.lower(addressesJoinOrigin(root).<String>get("city")), strToSearch + "%"),
                            cb.like(cb.lower(addressesJoinOrigin(root).<String>get("country")), strToSearch + "%")
                    );
                }
                return cb.like(cb.lower(root.get(searchCriteria.getKey())), strToSearch + "%");

            case DOES_NOT_START_WITH:
                if(searchCriteria.getKey().equals("address") )
                {
                    log.info("comparing CONTAINS origin {}", addressesJoinOrigin(root).<String>get("streetName"));
                    return cb.or(
                            cb.notLike(cb.lower(addressesJoinOrigin(root).<String>get("streetName")), strToSearch + "%"),
                            cb.notLike(cb.lower(addressesJoinOrigin(root).<String>get("city")), strToSearch + "%"),
                            cb.notLike(cb.lower(addressesJoinOrigin(root).<String>get("country")), strToSearch + "%")
                    );
                }
                return cb.notLike(cb.lower(root.get(searchCriteria.getKey())), strToSearch + "%");
            case ENDS_WITH:
                if(searchCriteria.getKey().equals("address") )
                {
                    log.info("comparing CONTAINS origin {}", addressesJoinOrigin(root).<String>get("streetName"));
                    return cb.or(
                            cb.like(cb.lower(addressesJoinOrigin(root).<String>get("streetName")), "%"+strToSearch ),
                            cb.like(cb.lower(addressesJoinOrigin(root).<String>get("city")), "%"+strToSearch ),
                            cb.like(cb.lower(addressesJoinOrigin(root).<String>get("country")), "%"+strToSearch )
                    );
                }
                return cb.like(cb.lower(root.get(searchCriteria.getKey())), "%"+strToSearch );

            case DOES_NOT_END_WITH:
                if(searchCriteria.getKey().equals("address") )
                {
                    log.info("comparing CONTAINS origin {}", addressesJoinOrigin(root).<String>get("streetName"));
                    return cb.or(
                            cb.notLike(cb.lower(addressesJoinOrigin(root).<String>get("streetName")), "%"+strToSearch ),
                            cb.notLike(cb.lower(addressesJoinOrigin(root).<String>get("city")), "%"+strToSearch ),
                            cb.notLike(cb.lower(addressesJoinOrigin(root).<String>get("country")), "%"+strToSearch )
                    );
                }
                return cb.notLike(cb.lower(root.get(searchCriteria.getKey())), "%"+strToSearch );
            case EQUAL:
                if(searchCriteria.getKey().equals("address") )
                {
                    log.info("comparing EQ origin {}", addressesJoinOrigin(root).<String>get("streetName"));
                    return cb.or(
                            cb.equal(cb.lower(addressesJoinOrigin(root).<String>get("streetName")), strToSearch ),
                            cb.equal(cb.lower(addressesJoinOrigin(root).<String>get("city")), strToSearch ),
                            cb.equal(cb.lower(addressesJoinOrigin(root).<String>get("country")), strToSearch )
                    );
                }
                return cb.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());

            case NOT_EQUAL:
                if(searchCriteria.getKey().equals("address") )
                {
                    log.info("comparing EQ origin {}", addressesJoinOrigin(root).<String>get("streetName"));
                    return cb.or(
                            cb.notEqual(cb.lower(addressesJoinOrigin(root).<String>get("streetName")), strToSearch ),
                            cb.notEqual(cb.lower(addressesJoinOrigin(root).<String>get("city")), strToSearch ),
                            cb.notEqual(cb.lower(addressesJoinOrigin(root).<String>get("country")), strToSearch )
                    );
                }
                return cb.notEqual(root.get(searchCriteria.getKey()), searchCriteria.getValue());
            case NUL:
                return cb.isNull(root.get(searchCriteria.getKey()));

            case NOT_NULL:
                return cb.isNotNull(root.get(searchCriteria.getKey()));

            case GREATER_THAN:
                return cb.greaterThan(root.<String> get(searchCriteria.getKey()), searchCriteria.getValue().toString());

            case GREATER_THAN_EQUAL:
                return cb.greaterThanOrEqualTo(root.<String> get(searchCriteria.getKey()), searchCriteria.getValue().toString());

            case LESS_THAN:
                return cb.lessThan(root.<String> get(searchCriteria.getKey()), searchCriteria.getValue().toString());

            case LESS_THAN_EQUAL:
                return cb.lessThanOrEqualTo(root.<String> get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        }
        return null;
    }

    private SetJoin<Customer, Address> addressesJoinOrigin(Root<Customer> root){
        return root.joinSet("addresses", JoinType.LEFT);
    }

}
