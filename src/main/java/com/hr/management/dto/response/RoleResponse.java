package com.hr.management.dto.response;

import com.hr.management.model.RoleType;
import lombok.Data;

/**
 * Data Transfer Object for Role response
 */
@Data
public class RoleResponse {

    private Long id;
    private RoleType type;
}
