package com.hr.management.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A SearchCriteria is a triple of:
 *
 *          key    - The Customer column name (JPA based)
 *          value  - The value being filtered
 *          filter - The filtering operation (it can be contains, equals, greater than, etc.)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {

    private String key;
    private Object value;
    private String filter;
    private String dataOption;

    public SearchCriteria(String key, String filter, Object value){
        super();
        this.key = key;
        this.filter = filter;
        this.value = value;
    }
}
