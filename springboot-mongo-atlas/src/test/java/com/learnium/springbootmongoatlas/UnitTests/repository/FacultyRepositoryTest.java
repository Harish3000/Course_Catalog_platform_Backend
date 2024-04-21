package com.learnium.springbootmongoatlas.UnitTests.repository;

import com.learnium.model.Faculty;
import com.learnium.repository.FacultyRepository;
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
class FacultyRepositoryTest {

    @Autowired
    private FacultyRepository facultyRepository;

    private Faculty faculty;

    @BeforeEach
    void setUp() {
        faculty = new Faculty();
        faculty.setFacultyId("FAC-001");
        faculty.setFacultyName("Test Faculty");
        facultyRepository.save(faculty);
    }

    @AfterEach
    void tearDown() {
        facultyRepository.deleteAll();
    }

    @Test
    void whenValidFacultyId_thenFacultyShouldExist() {
        assertTrue(facultyRepository.existsByFacultyId("FAC-001"));
    }

    @Test
    void whenInvalidFacultyId_thenFacultyShouldNotExist() {
        assertFalse(facultyRepository.existsByFacultyId("INVALID"));
    }
}