package com.assignment.dataset.service.impl;

import com.assignment.dataset.dto.RecordInsertResponse;
import com.assignment.dataset.entity.DatasetRecordEntity;
import com.assignment.dataset.exception.InvalidQueryParameterException;
import com.assignment.dataset.exception.ResourceNotFoundException;
import com.assignment.dataset.repository.DatasetRecordRepository;
import com.assignment.dataset.service.DatasetService;
import com.assignment.dataset.strategy.DatasetQueryStrategy;
import com.assignment.dataset.strategy.QueryContext;
import com.assignment.dataset.strategy.QueryStrategyFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DatasetServiceImpl implements DatasetService {

    private final DatasetRecordRepository repository;
    private final QueryStrategyFactory strategyFactory;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public RecordInsertResponse insertRecord(String datasetName, JsonNode recordPayload) {
        if (recordPayload == null || recordPayload.isNull() || !recordPayload.isObject()) {
            throw new InvalidQueryParameterException("Record payload must be a non-empty JSON object.");
        }

        Object recordIdObj = null;
        if (recordPayload.has("id")) {
            JsonNode idNode = recordPayload.get("id");
            if (idNode.isNumber()) {
                recordIdObj = idNode.numberValue();
            } else {
                recordIdObj = idNode.asText();
            }
        } else {
            recordIdObj = UUID.randomUUID().toString();
        }

        DatasetRecordEntity entity = DatasetRecordEntity.builder()
                .datasetName(datasetName)
                .recordId(String.valueOf(recordIdObj))
                .recordData(recordPayload.toString())
                .build();

        repository.save(entity);

        return RecordInsertResponse.builder()
                .message("Record added successfully")
                .dataset(datasetName)
                .recordId(recordIdObj)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Object queryDataset(String datasetName, String groupBy, String sortBy, String order) {
        if (!repository.existsByDatasetName(datasetName)) {
            throw new ResourceNotFoundException("Dataset '" + datasetName + "' does not exist.");
        }

        List<DatasetRecordEntity> rawEntities = repository.findByDatasetName(datasetName);
        List<Map<String, Object>> parsedRecords = new ArrayList<>();

        for (DatasetRecordEntity entity : rawEntities) {
            try {
                Map<String, Object> recordMap = objectMapper.readValue(
                        entity.getRecordData(),
                        new TypeReference<>() {}
                );
                parsedRecords.add(recordMap);
            } catch (Exception ignored) {
            }
        }

        QueryContext context = QueryContext.builder()
                .datasetName(datasetName)
                .groupByField(groupBy)
                .sortByField(sortBy)
                .sortOrder(order)
                .records(parsedRecords)
                .build();

        DatasetQueryStrategy strategy = strategyFactory.getStrategy(context);
        return strategy.execute(context);
    }
}