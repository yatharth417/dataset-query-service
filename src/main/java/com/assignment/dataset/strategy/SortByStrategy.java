package com.assignment.dataset.strategy;

import com.assignment.dataset.dto.SortedQueryResponse;
import com.assignment.dataset.exception.InvalidQueryParameterException;
import com.assignment.dataset.util.JsonNodeComparator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SortByStrategy implements DatasetQueryStrategy {

    @Override
    public boolean supports(QueryContext context) {
        return context.getSortByField() != null && !context.getSortByField().isBlank();
    }

    @Override
    public Object execute(QueryContext context) {
        String sortField = context.getSortByField();
        String order = context.getSortOrder();

        boolean ascending = true;
        if (order != null && !order.isBlank()) {
            if ("asc".equalsIgnoreCase(order)) {
                ascending = true;
            } else if ("desc".equalsIgnoreCase(order)) {
                ascending = false;
            } else {
                throw new InvalidQueryParameterException("Invalid order parameter '" + order + "'. Allowed values are: 'asc', 'desc'.");
            }
        }

        List<Map<String, Object>> sorted = new ArrayList<>(context.getRecords());
        sorted.sort(new JsonNodeComparator(sortField, ascending));

        return SortedQueryResponse.builder()
                .sortedRecords(sorted)
                .build();
    }
}