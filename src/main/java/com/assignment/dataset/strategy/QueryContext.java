package com.assignment.dataset.strategy;

import lombok.Builder;
import lombok.Getter;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class QueryContext {
    private String datasetName;
    private String groupByField;
    private String sortByField;
    private String sortOrder;
    private List<Map<String, Object>> records;
}