package com.hr.management.controller;

import com.hr.management.IntegrationTest;
import com.hr.management.dto.mapper.CustomerRequestMapper;
import com.hr.management.dto.mapper.CustomerResponseMapper;
import com.hr.management.dto.request.AddressRequest;
import com.hr.management.dto.request.CustomerRequest;
import com.hr.management.dto.response.ApiResponse;
import com.hr.management.dto.response.CustomerResponse;
import com.hr.management.search.CustomerSearchDTO;
import com.hr.management.search.SearchCriteria;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import  com.fasterxml.jackson.databind.JavaType;

class CustomerControllerTest extends IntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private CustomerRequestMapper customerRequestMapper;

    @Mock
    private CustomerResponseMapper customerResponseMapper;

    /**
     * Method under test: {@link CustomerController#findById(long)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findById_should_returnStatusIsOk_when_IsFound() throws Exception {
        mvc.perform((get("/api/v1/customers/{id}", 1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName", equalTo("Julia")));
    }

    /**
     * Method under test: {@link CustomerController#findById(long)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findById_should_returnStatusIsNotFound_when_IsNotFound() throws Exception {
        mvc.perform((get("/api/v1/customers/{id}", 999)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    /**
     * Method under test: {@link CustomerController#findAll(int, int, String, String, CustomerSearchDTO)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findAll_Customer_Search_AgeGreaterThan88_should_return_1_Element_And_StatusIsOk() throws Exception {

        CustomerSearchDTO customerSearchDTO = new CustomerSearchDTO();
        customerSearchDTO.setDataOption("all");
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .key("age")
                .filter("gt")
                .value(88)
                .build();

        searchCriteriaList.add(searchCriteria);
        customerSearchDTO.setSearchCriteriaList(searchCriteriaList);
        mvc.perform((get("/api/v1/customers/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerSearchDTO))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName").value("Eva"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findAll_Customer_Search_AgeGreaterThan16_should_return_3_Element_And_StatusIsOk() throws Exception {

        CustomerSearchDTO customerSearchDTO = new CustomerSearchDTO();
        customerSearchDTO.setDataOption("all");
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .key("age")
                .filter("gt")
                .value(16)
                .build();

        searchCriteriaList.add(searchCriteria);
        customerSearchDTO.setSearchCriteriaList(searchCriteriaList);
        mvc.perform((get("/api/v1/customers/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerSearchDTO))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName").value("Eva"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(3));
    }

    /**
     * Method under test: {@link CustomerController#findAll(int, int, String, String, CustomerSearchDTO)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findAll_Customer_Search_CheckContents_Customers_should_returnStatusIsOk() throws Exception {

        CustomerSearchDTO customerSearchDTO = new CustomerSearchDTO();
        customerSearchDTO.setDataOption("all");
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        SearchCriteria searchCriteria = SearchCriteria.builder()
                        .key("age")
                        .filter("le")
                        .value(15).build();
        searchCriteriaList.add(searchCriteria);
        customerSearchDTO.setSearchCriteriaList(searchCriteriaList);
        mvc.perform((get("/api/v1/customers/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerSearchDTO))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].lastName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].lastName").value("Japhe"));
    }

    /**
     * Method under test: {@link CustomerController#findAll(int, int, String, String, CustomerSearchDTO)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findAll_Customer_Search_CheckContents_Customers_2_Customers() throws Exception {

        CustomerSearchDTO customerSearchDTO = new CustomerSearchDTO();
        customerSearchDTO.setDataOption("all");
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();

        // searches for a customer with a given address (streetName)
        SearchCriteria searchCriteria =  SearchCriteria.builder().key("address").filter("cn").value("Naef").build();
        searchCriteriaList.add(searchCriteria);
        customerSearchDTO.setSearchCriteriaList(searchCriteriaList);
        mvc.perform((get("/api/v1/customers/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerSearchDTO))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName").isNotEmpty());
    }

    /**
     * Method under test: {@link CustomerController#create(CustomerRequest)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void create_Customer_Customers_Should_Return201() throws Exception {

        CustomerRequest req = CustomerRequest.builder()
                .firstName("Mikael")
                .lastName("van Dirk")
                .age(43)
                        .addresses(Set.of(
                                AddressRequest.builder().streetName("Top").city("Amstelveen").build(),
                                AddressRequest.builder().streetName("Postjesweg").city("Amsterdam").build()
                                ))
                .build();
        
        mvc.perform((post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.age").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName").value("van Dirk"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.age").value(43));
    }

    /**
     * Method under test: {@link CustomerController#create(CustomerRequest)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void create_And_Get_Customer_Customers_Should_Return201_And_ThenGET200() throws Exception {

        CustomerRequest req = CustomerRequest.builder()
                .firstName("Mikael")
                .lastName("van Dirk")
                .age(43)
                .addresses(Set.of(
                        AddressRequest.builder().streetName("Top").city("Amstelveen").build(),
                        AddressRequest.builder().streetName("Postjesweg").city("Amsterdam").build()
                ))
                .build();

        MvcResult res = mvc.perform((post("/api/v1/customers")
                        .contentType("application/json")
                .accept("application/json;charset=UTF-8")
                        .content(objectMapper.writeValueAsString(req))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName").value("van Dirk"))
                        .andReturn();

        JavaType type = objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, CustomerResponse.class);

        ApiResponse<CustomerResponse> r = objectMapper.readValue(res.getResponse().getContentAsString(), type);

        mvc.perform((get("/api/v1/customers/{id}", r.getData().getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName").value("Mikael"));
    }


    /**
     * Method under test: {@link CustomerController#create(CustomerRequest)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void create_And_Delete_Customer_Customers_Should_Return201_And_ThenDelete200() throws Exception {

        CustomerRequest req = CustomerRequest.builder()
                .firstName("Lino")
                .lastName("van Helm")
                .age(43)
                .addresses(Set.of(
                        AddressRequest.builder().streetName("Top").city("Amstelveen").build(),
                        AddressRequest.builder().streetName("Postjesweg").city("Amsterdam").build()
                ))
                .build();


        MvcResult res = mvc.perform((post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName").value("Lino"))
                .andReturn();

        JavaType type = objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, CustomerResponse.class);

        ApiResponse<CustomerResponse> r = objectMapper.readValue(res.getResponse().getContentAsString(), type);

        mvc.perform((delete("/api/v1/customers/{id}", r.getData().getId())))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    /**
     * Method under test: {@link CustomerController#create(CustomerRequest)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void create_And_PUT_Customer_Customers_Should_Return201_And_ThenPut200() throws Exception {

        CustomerRequest req = CustomerRequest.builder()
                .firstName("Gepeto")
                .lastName("voor Naam")
                .age(43)
                .addresses(Set.of(
                        AddressRequest.builder().streetName("Top").city("Amstelveen").build(),
                        AddressRequest.builder().streetName("Postjesweg").city("Amsterdam").build()
                ))
                .build();


        // Create the Customer for Banana Au Termidor
        MvcResult res = mvc.perform((post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName").value("voor Naam"))
                .andReturn();

        JavaType type = objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, CustomerResponse.class);

        ApiResponse<CustomerResponse> r = objectMapper.readValue(res.getResponse().getContentAsString(), type);

        // change the Customer name to a different name
        r.getData().setFirstName("Marcos");

        mvc.perform((put("/api/v1/customers")
                        .content(objectMapper.writeValueAsString(r.getData()))
                        .contentType("application/json")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(r.getData().getId()));
    }


}
