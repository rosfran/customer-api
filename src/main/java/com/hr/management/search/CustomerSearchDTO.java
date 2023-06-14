package com.hr.management.search;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSearchDTO {

    /**
     * List of SearchCriterias, where each one must fill: key, filter and value
     */
    private List<SearchCriteria> searchCriteriaList;

    /**
     * dataOption is to say if all searchCriterias must be comprised, or only a set of them
     */
    private String dataOption;

}