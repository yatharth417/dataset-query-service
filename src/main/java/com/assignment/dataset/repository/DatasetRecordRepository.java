package com.assignment.dataset.repository;

import com.assignment.dataset.entity.DatasetRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DatasetRecordRepository extends JpaRepository<DatasetRecordEntity, Long> {
    List<DatasetRecordEntity> findByDatasetName(String datasetName);
    boolean existsByDatasetName(String datasetName);
}