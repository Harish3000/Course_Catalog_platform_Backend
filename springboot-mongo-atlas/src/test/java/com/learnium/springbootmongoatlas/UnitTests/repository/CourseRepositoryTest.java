package com.learnium.springbootmongoatlas.UnitTests.repository;

import com.learnium.model.Course;
import com.learnium.repository.CourseRepository;
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
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setCourseCode("TEST");
        courseRepository.save(course);
    }

    @AfterEach
    void tearDown() {
        courseRepository.deleteAll();
    }

    @Test
    void whenValidCourseCode_thenCourseShouldExist() {
        assertTrue(courseRepository.existsByCourseCode("TEST"));
    }

    @Test
    void whenInvalidCourseCode_thenCourseShouldNotExist() {
        assertFalse(courseRepository.existsByCourseCode("INVALID"));
    }
}