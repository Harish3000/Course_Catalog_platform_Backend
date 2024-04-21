package com.learnium.springbootmongoatlas.UnitTests.repository;

import com.learnium.model.Enrollment;
import com.learnium.repository.EnrollmentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        enrollment = new Enrollment();
        enrollment.setEnrollmentId("ENR-001");
        enrollment.setCourseCode("TEST");
        enrollment.setStudentId(Arrays.asList("STU-001", "STU-002"));
        enrollmentRepository.save(enrollment);
    }

    @AfterEach
    void tearDown() {
        enrollmentRepository.deleteAll();
    }

    @Test
    void whenValidStudentIdAndCourseCode_thenEnrollmentShouldExist() {
        assertTrue(enrollmentRepository.existsByStudentIdAndCourseCode("STU-001", "TEST"));
    }

    @Test
    void whenInvalidStudentIdAndCourseCode_thenEnrollmentShouldNotExist() {
        assertFalse(enrollmentRepository.existsByStudentIdAndCourseCode("INVALID", "TEST"));
    }
}