package com.assignment.dataset.service;

import com.assignment.dataset.dto.GroupedQueryResponse;
import com.assignment.dataset.dto.RecordInsertResponse;
import com.assignment.dataset.entity.DatasetRecordEntity;
import com.assignment.dataset.exception.ResourceNotFoundException;
import com.assignment.dataset.repository.DatasetRecordRepository;
import com.assignment.dataset.service.impl.DatasetServiceImpl;
import com.assignment.dataset.strategy.GroupByStrategy;
import com.assignment.dataset.strategy.QueryStrategyFactory;
import com.assignment.dataset.strategy.SortByStrategy;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatasetServiceTest {

    @Mock
    private DatasetRecordRepository repository;

    private DatasetService datasetService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        QueryStrategyFactory factory = new QueryStrategyFactory(
                List.of(new GroupByStrategy(), new SortByStrategy())
        );
        datasetService = new DatasetServiceImpl(repository, factory, objectMapper);
    }

    @Test
    @DisplayName("Successfully insert a record")
    void testInsertRecord() throws Exception {
        JsonNode payload = objectMapper.readTree("{\"id\": 10, \"name\": \"Test\", \"dept\": \"IT\"}");

        when(repository.save(any(DatasetRecordEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RecordInsertResponse response = datasetService.insertRecord("dept_db", payload);

        assertNotNull(response);
        assertEquals("dept_db", response.getDataset());
        assertEquals(10, response.getRecordId());
        verify(repository, times(1)).save(any(DatasetRecordEntity.class));
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when querying non-existent dataset")
    void testQueryNonExistentDataset() {
        when(repository.existsByDatasetName("unknown")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                datasetService.queryDataset("unknown", "dept", null, "asc")
        );
    }

    @Test
    @DisplayName("Query grouping returns valid GroupedQueryResponse")
    void testQueryGrouping() {
        when(repository.existsByDatasetName("emp")).thenReturn(true);
        DatasetRecordEntity entity1 = DatasetRecordEntity.builder()
                .datasetName("emp")
                .recordId("1")
                .recordData("{\"id\": 1, \"dept\": \"HR\"}")
                .build();
        when(repository.findByDatasetName("emp")).thenReturn(List.of(entity1));

        Object result = datasetService.queryDataset("emp", "dept", null, "asc");

        assertTrue(result instanceof GroupedQueryResponse);
        GroupedQueryResponse grouped = (GroupedQueryResponse) result;
        assertTrue(grouped.getGroupedRecords().containsKey("HR"));
    }
}