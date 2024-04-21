package com.learnium.controller;

import com.learnium.model.Faculty;
import com.learnium.service.FacultyService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty")
public class FacultyController {

    private static final Logger logger = LoggerFactory.getLogger(FacultyController.class);

    @Autowired
    private FacultyService service;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Faculty createFaculty(@Valid @RequestBody Faculty faculty){
        try {
            Faculty createdFaculty = service.addFaculty(faculty);
            logger.info("Faculty created successfully");
            return createdFaculty;
        } catch (Exception e) {
            logger.error("Error occurred while creating faculty", e);
            throw e;
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER') or hasAuthority('ROLE_FACULTY')")
    public List<Faculty> getFacultys() {
        try {
            return service.findAllFacultys();
        } catch (Exception e) {
            logger.error("Error occurred while fetching all faculties", e);
            throw e;
        }
    }

    @GetMapping("/{facultyId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER') or hasAuthority('ROLE_FACULTY')")
    public Faculty getFaculty(@PathVariable String facultyId){
        try {
            return service.getFacultyByFacultyId(facultyId);
        } catch (Exception e) {
            logger.error("Error occurred while fetching faculty", e);
            throw e;
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Faculty modifyFaculty(@Valid @RequestBody Faculty faculty){
        try {
            return service.updateFaculty(faculty);
        } catch (Exception e) {
            logger.error("Error occurred while updating faculty", e);
            throw e;
        }
    }

    @DeleteMapping("/delete/{facultyId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteFaculty(@PathVariable String facultyId){
        try {
            return service.deleteFaculty(facultyId);
        } catch (Exception e) {
            logger.error("Error occurred while deleting faculty", e);
            throw e;
        }
    }
}