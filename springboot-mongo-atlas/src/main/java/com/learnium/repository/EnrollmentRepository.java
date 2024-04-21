package com.learnium.repository;

import com.learnium.model.Enrollment;
import com.learnium.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;

public interface EnrollmentRepository extends MongoRepository<Enrollment,String> {
    boolean existsByStudentIdAndCourseCode(String studentId, String courseCode);
}
