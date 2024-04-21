package com.learnium.repository;

import com.learnium.model.Course;
import com.learnium.model.Faculty;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FacultyRepository extends MongoRepository<Faculty,String> {
    List<Course> getCourseByfacultyId(String facultyId);
    boolean existsByFacultyId(String facultyId);
}
