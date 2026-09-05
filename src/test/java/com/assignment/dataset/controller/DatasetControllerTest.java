package com.assignment.dataset.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DatasetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Verify complete workflow: Insert records, GroupBy query, and SortBy query")
    void testFullWorkflow() throws Exception {
        String record1 = """
            {
                "id": 1,
                "name": "John Doe",
                "age": 30,
                "department": "Engineering"
            }
        """;
        mockMvc.perform(post("/api/dataset/employee_dataset/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(record1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Record added successfully"))
                .andExpect(jsonPath("$.dataset").value("employee_dataset"))
                .andExpect(jsonPath("$.recordId").value(1));

        String record2 = """
            {
                "id": 2,
                "name": "Jane Smith",
                "age": 25,
                "department": "Engineering"
            }
        """;
        mockMvc.perform(post("/api/dataset/employee_dataset/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(record2))
                .andExpect(status().isOk());

        String record3 = """
            {
                "id": 3,
                "name": "Alice Brown",
                "age": 28,
                "department": "Marketing"
            }
        """;
        mockMvc.perform(post("/api/dataset/employee_dataset/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(record3))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/dataset/employee_dataset/query")
                        .param("groupBy", "department"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupedRecords.Engineering").isArray())
                .andExpect(jsonPath("$.groupedRecords.Engineering.length()").value(2))
                .andExpect(jsonPath("$.groupedRecords.Marketing.length()").value(1));

        mockMvc.perform(get("/api/dataset/employee_dataset/query")
                        .param("sortBy", "age")
                        .param("order", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sortedRecords[0].age").value(25))
                .andExpect(jsonPath("$.sortedRecords[1].age").value(28))
                .andExpect(jsonPath("$.sortedRecords[2].age").value(30));
    }

    @Test
    @DisplayName("Should return 400 when neither groupBy nor sortBy is supplied")
    void shouldReturnBadRequestForMissingParams() throws Exception {
        mockMvc.perform(post("/api/dataset/dummy/record")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 10}"));

        mockMvc.perform(get("/api/dataset/dummy/query"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    @DisplayName("Should return 404 when querying non-existent dataset")
    void shouldReturnNotFoundForMissingDataset() throws Exception {
        mockMvc.perform(get("/api/dataset/non_existent/query")
                        .param("groupBy", "department"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}