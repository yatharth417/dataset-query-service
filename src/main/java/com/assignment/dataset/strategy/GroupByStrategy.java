package com.assignment.dataset.strategy;

import com.assignment.dataset.dto.GroupedQueryResponse;
import com.assignment.dataset.util.JsonNodeComparator;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GroupByStrategy implements DatasetQueryStrategy {

    @Override
    public boolean supports(QueryContext context) {
        return context.getGroupByField() != null && !context.getGroupByField().isBlank();
    }

    @Override
    public Object execute(QueryContext context) {
        String field = context.getGroupByField();

        Map<String, List<Map<String, Object>>> grouped = context.getRecords().stream()
                .collect(Collectors.groupingBy(
                        record -> {
                            Object val = JsonNodeComparator.resolveValue(record, field);
                            return val != null ? String.valueOf(val) : "null";
                        },
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        return GroupedQueryResponse.builder()
                .groupedRecords(grouped)
                .build();
    }
}