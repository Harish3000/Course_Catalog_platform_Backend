package com.learnium.repository;

import com.learnium.model.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;

public interface ResourceRepository extends MongoRepository<Resource,String> {
    boolean existsByResourceIdAndReservedDate(String resourceId, LocalDate date);
    boolean existsByReservedDate(LocalDate date);
    boolean existsByResourceId(String resourceId);
}
