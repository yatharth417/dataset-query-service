package com.assignment.dataset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupedQueryResponse {
    private Map<String, List<Map<String, Object>>> groupedRecords;
}