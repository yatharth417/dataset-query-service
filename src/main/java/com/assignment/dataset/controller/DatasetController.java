package com.assignment.dataset.controller;

import com.assignment.dataset.dto.RecordInsertResponse;
import com.assignment.dataset.service.DatasetService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dataset")
@RequiredArgsConstructor
public class DatasetController {

    private final DatasetService datasetService;

    @PostMapping("/{datasetName}/record")
    public ResponseEntity<RecordInsertResponse> insertRecord(
            @PathVariable("datasetName") String datasetName,
            @RequestBody JsonNode record) {
        RecordInsertResponse response = datasetService.insertRecord(datasetName, record);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{datasetName}/query")
    public ResponseEntity<Object> queryDataset(
            @PathVariable("datasetName") String datasetName,
            @RequestParam(value = "groupBy", required = false) String groupBy,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order) {
        Object response = datasetService.queryDataset(datasetName, groupBy, sortBy, order);
        return ResponseEntity.ok(response);
    }
}