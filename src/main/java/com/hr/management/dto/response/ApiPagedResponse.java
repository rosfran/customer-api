package com.hr.management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the API response in a  format with  message and data
 *
 * @param <T>
 */
@Data
@AllArgsConstructor
public class ApiPagedResponse<T> {

    private final String message;
    private final T data;
    private final int page;
    private final int pageSize;
    private final int totalElements;

    public ApiPagedResponse(String message) {
        this.message = message;
        this.data = null;
        this.page = 0;
        this.pageSize = 0;
        this.totalElements = 0;
    }
}
