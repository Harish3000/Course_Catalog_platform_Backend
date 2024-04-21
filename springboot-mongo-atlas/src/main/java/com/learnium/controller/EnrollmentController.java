package com.learnium.controller;

import com.learnium.model.Enrollment;
import com.learnium.service.EnrollmentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

    @Autowired
    private EnrollmentService service;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Enrollment createEnrollment(@Valid @RequestBody Enrollment enrollment){
        try {
            Enrollment createdEnrollment = service.addEnrollment(enrollment);
            logger.info("Enrollment created successfully");
            return createdEnrollment;
        } catch (Exception e) {
            logger.error("Error occurred while creating enrollment", e);
            throw e;
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_FACULTY')")
    public List<Enrollment> getEnrollments() {
        try {
            return service.findAllEnrollments();
        } catch (Exception e) {
            logger.error("Error occurred while fetching all enrollments", e);
            throw e;
        }
    }

    @GetMapping("/{enrollmentId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_FACULTY')")
    public Enrollment getEnrollment(@PathVariable String enrollmentId){
        try {
            return service.getEnrollmentByEnrollmentId(enrollmentId);
        } catch (Exception e) {
            logger.error("Error occurred while fetching enrollment", e);
            throw e;
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Enrollment modifyEnrollment(@Valid @RequestBody Enrollment enrollment){
        try {
            return service.updateEnrollment(enrollment);
        } catch (Exception e) {
            logger.error("Error occurred while updating enrollment", e);
            throw e;
        }
    }

    @DeleteMapping("/delete/{enrollmentId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteEnrollment(@PathVariable String enrollmentId){
        try {
            return service.deleteEnrollment(enrollmentId);
        } catch (Exception e) {
            logger.error("Error occurred while deleting enrollment", e);
            throw e;
        }
    }
}