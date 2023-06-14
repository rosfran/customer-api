package com.hr.management.controller;

import com.hr.management.IntegrationTest;

import com.hr.management.dto.mapper.UserRequestMapper;
import com.hr.management.dto.mapper.UserResponseMapper;

import com.hr.management.dto.response.ApiResponse;
import com.hr.management.dto.response.UserResponse;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends IntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserRequestMapper userRequestMapper;

    @Mock
    private UserResponseMapper userResponseMapper;

    /**
     * Method under test: {@link UserController.findById(long)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findUserById_should_returnStatusIsOk_when_IsFound() throws Exception {
        MvcResult res = mvc.perform((get("/api/v1/users/{id}", 1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username", equalTo("johndoe")))
                .andReturn();

        JavaType type = objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, UserResponse.class);

        ApiResponse<UserResponse> response = objectMapper.readValue(res.getResponse().getContentAsString(), type);


        assertEquals("johndoe", response.getData().getUsername());

        assertNotNull(response.getData().getRoles());

        assertEquals(2, response.getData().getRoles().size());
    }

    /**
     * Method under test: {@link UserController.findById(long)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findUserById_should_returnStatusIsOk_when_OtherUser_IsFound() throws Exception {
        MvcResult res = mvc.perform((get("/api/v1/users/{id}", 2)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username", equalTo("jake")))
                .andReturn();

        JavaType type = objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, UserResponse.class);

        ApiResponse<UserResponse> response = objectMapper.readValue(res.getResponse().getContentAsString(), type);

        assertEquals("jake", response.getData().getUsername());

        assertEquals(new UserResponse("jake"), response.getData());

        assertNotNull(response.getData().getRoles());

        assertEquals(2, response.getData().getRoles().size());
    }

}
