package com.hr.management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the API response in a  format with  message and data
 *
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ApiResponse<T> {

    //private Long timestamp;
    private final String message;
    private final T data;

    public ApiResponse(Long timestamp, String message) {
        //this.timestamp = timestamp;
        this.message = message;
        this.data = null;
    }
}
