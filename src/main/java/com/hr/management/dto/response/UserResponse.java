package com.hr.management.dto.response;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Data Transfer Object for User response
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(of="username")
public class UserResponse {

    public UserResponse(String username) { this.username = username; }
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String fullName;
    private Set<RoleResponse> roles;
}
