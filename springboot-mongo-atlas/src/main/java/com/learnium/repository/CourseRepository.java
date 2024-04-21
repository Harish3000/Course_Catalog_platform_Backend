package com.learnium.repository;

import com.learnium.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CourseRepository extends MongoRepository<Course,String> {
    boolean existsByCourseCode(String courseCode);
}
