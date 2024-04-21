package com.learnium.springbootmongoatlas.UnitTests.repository;

import com.learnium.model.Resource;
import com.learnium.repository.ResourceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class ResourceRepositoryTest {

    @Autowired
    private ResourceRepository resourceRepository;

    private Resource resource;

    @BeforeEach
    void setUp() {
        resource = new Resource();
        resource.setResourceId("RES-001");
        resource.setReservedDate(LocalDate.now());
        resourceRepository.save(resource);
    }

    @AfterEach
    void tearDown() {
        resourceRepository.deleteAll();
    }

    @Test
    void whenValidResourceIdAndReservedDate_thenResourceShouldExist() {
        assertTrue(resourceRepository.existsByResourceIdAndReservedDate("RES-001", LocalDate.now()));
    }

    @Test
    void whenInvalidResourceIdAndReservedDate_thenResourceShouldNotExist() {
        assertFalse(resourceRepository.existsByResourceIdAndReservedDate("INVALID", LocalDate.now()));
    }
}