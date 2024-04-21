package com.learnium.controller;

import com.learnium.model.Course;
import com.learnium.service.CourseService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService service;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Course createCourse(@Valid @RequestBody Course course){
        try {
            Course createdCourse = service.addCourse(course);
            logger.info("Course created successfully");
            return createdCourse;
        } catch (Exception e) {
            logger.error("Error occurred while creating course", e);
            throw e;
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER') or hasAuthority('ROLE_FACULTY')")
    public List<Course> getCourses() {
        try {
            return service.findAllCourses();
        } catch (Exception e) {
            logger.error("Error occurred while fetching all courses", e);
            throw e;
        }
    }

    @GetMapping("/{courseId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER') or hasAuthority('ROLE_FACULTY')")
    public Course getCourse(@PathVariable String courseId){
        try {
            return service.getCourseByCourseId(courseId);
        } catch (Exception e) {
            logger.error("Error occurred while fetching course", e);
            throw e;
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Course modifyCourse(@Valid @RequestBody Course course){
        try {
            return service.updateCourse(course);
        } catch (Exception e) {
            logger.error("Error occurred while updating course", e);
            throw e;
        }
    }

    @DeleteMapping("/delete/{courseId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteCourse(@PathVariable String courseId){
        try {
            return service.deleteCourse(courseId);
        } catch (Exception e) {
            logger.error("Error occurred while deleting course", e);
            throw e;
        }
    }
}