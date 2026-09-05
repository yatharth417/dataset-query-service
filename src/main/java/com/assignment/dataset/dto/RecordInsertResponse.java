package com.assignment.dataset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordInsertResponse {
    private String message;
    private String dataset;
    private Object recordId;
}