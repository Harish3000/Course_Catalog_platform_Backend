package com.learnium.springbootmongoatlas.UnitTests.repository;

import com.learnium.model.TimeTable;
import com.learnium.repository.TimeTableRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class TimeTableRepositoryTest {

    @Autowired
    private TimeTableRepository timeTableRepository;

    private TimeTable timeTable;

    @BeforeEach
    void setUp() {
        timeTable = new TimeTable();
        timeTable.setCourseCode("TEST");
        timeTableRepository.save(timeTable);
    }

    @AfterEach
    void tearDown() {
        timeTableRepository.deleteAll();
    }

    @Test
    void whenValidCourseCode_thenTimeTableShouldExist() {
        assertTrue(timeTableRepository.existsByCourseCode("TEST"));
    }

    @Test
    void whenInvalidCourseCode_thenTimeTableShouldNotExist() {
        assertFalse(timeTableRepository.existsByCourseCode("INVALID"));
    }
}