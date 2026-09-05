package com.assignment.dataset.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(
    name = "dataset_records",
    indexes = {
        @Index(name = "idx_dataset_name", columnList = "datasetName"),
        @Index(name = "idx_dataset_record_id", columnList = "datasetName, recordId")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatasetRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String datasetName;

    @Column(nullable = false)
    private String recordId;

    @Lob
    @Column(nullable = false, columnDefinition = "CLOB")
    private String recordData;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }
}