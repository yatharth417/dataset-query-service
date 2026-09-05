package com.assignment.dataset.service;

import com.assignment.dataset.dto.RecordInsertResponse;
import com.fasterxml.jackson.databind.JsonNode;

public interface DatasetService {
    RecordInsertResponse insertRecord(String datasetName, JsonNode recordPayload);
    Object queryDataset(String datasetName, String groupBy, String sortBy, String order);
}