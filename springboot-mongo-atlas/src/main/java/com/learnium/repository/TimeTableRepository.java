package com.learnium.repository;

import com.learnium.model.Course;
import com.learnium.model.TimeTable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TimeTableRepository extends MongoRepository<TimeTable,String> {

    boolean existsByCourseCode(String courseCode);

}
