package com.hr.management.controller;

import com.hr.management.dto.request.CustomerRequest;
import com.hr.management.dto.response.ApiPagedResponse;
import com.hr.management.dto.response.ApiResponse;
import com.hr.management.dto.response.CommandResponse;
import com.hr.management.dto.response.CustomerResponse;
import com.hr.management.search.CustomerSearchBuilder;
import com.hr.management.search.CustomerSearchDTO;
import com.hr.management.search.SearchCriteria;
import com.hr.management.service.CustomerService;
import com.hr.management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.util.List;

import static com.hr.management.common.Constants.SUCCESS;

/**
 * A Customer is the minimum unit for producing energy. It needs to be
 * installed for at least 60 days before starting to produce energy
 *
 * This class implements some endpoints to Create, Update and Delete customers
 */
@Slf4j(topic = "CustomerController")
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final Clock clock;
    private final CustomerService customerService;
    private final UserService userService;

    /**
     * Fetches a single Customer by the given id
     *
     * @param id            The Customer ID
     * @return CustomerResponse
     */
    @PreAuthorize("hasRole(T(com.hr.management.model.RoleType).ROLE_USER)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> findById(@PathVariable long id) {
        final CustomerResponse response = customerService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>( SUCCESS, response));
    }

    /**
     * Do a parametrized search using any available parameters from a Customer
     *
     * @param pageNum           Page number
     * @param pageSize          Page size (amount of elements per page)
     * @param sort              Sorting field
     * @param order             Order (can be ascending or descending)
     * @param customerSearchDTO   Search DTO based on the incoming HTTP Request Body
     * @return
     */
    @PreAuthorize("hasRole(T(com.hr.management.model.RoleType).ROLE_USER)")
    @GetMapping("/search")
    public ResponseEntity<ApiPagedResponse<List<CustomerResponse>>> findAll(
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sort", defaultValue = "firstName") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            @Valid @RequestBody CustomerSearchDTO customerSearchDTO) {

        // create the SearchBuilder, and feed it with all the criterias gotten from RequestBody
        CustomerSearchBuilder criteriaBuilder = new CustomerSearchBuilder();

        if ( customerSearchDTO == null ) {
            customerSearchDTO = new CustomerSearchDTO();
        }
        List<SearchCriteria> criteriaList = customerSearchDTO.getSearchCriteriaList();
        if (criteriaList != null) {
            CustomerSearchDTO finalCustomerSearchDTO = customerSearchDTO;
            criteriaList.forEach(x -> {
                x.setDataOption(finalCustomerSearchDTO.getDataOption());
                criteriaBuilder.with(x);
            });
        }
        Sort s = Sort.by(sort);

        if ( order.toLowerCase().startsWith("asc")) {
            s = s.ascending();
        } else {
            s = s.descending();
        }

        Pageable page = PageRequest.of(pageNum, pageSize, s);

        List<CustomerResponse> result = customerService.findBySearchCriteria(criteriaBuilder.build(), page);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiPagedResponse<>( SUCCESS,  result, pageNum, pageSize, result.size()));
    }

    /**
     * Creates a new customers using the given request parameters
     *
     * @param request
     * @return id of the created type
     */
    @PreAuthorize("hasRole(T(com.hr.management.model.RoleType).ROLE_USER)")
    @PostMapping
    //@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<CustomerResponse>> create(@Valid @RequestBody CustomerRequest request) {
        final CustomerResponse response = customerService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>( SUCCESS, response));
    }

    /**
     * Updates a given Customer using the given request parameters
     *
     * @return id of the updated type
     */
    @PreAuthorize("hasRole(T(com.hr.management.model.RoleType).ROLE_USER)")
    @PutMapping
    public ResponseEntity<ApiResponse<CommandResponse>> update(@Valid @RequestBody CustomerRequest request) {
        final CommandResponse response = customerService.update(request);
        return ResponseEntity.ok(new ApiResponse<>( SUCCESS, response));
    }

    /**
     * Deletes Customer by id
     *
     * @param id
     */
    @PreAuthorize("hasRole(T(com.hr.management.model.RoleType).ROLE_USER)")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable long id) {
        customerService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
