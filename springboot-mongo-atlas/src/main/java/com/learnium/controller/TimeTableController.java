package com.learnium.controller;

import com.learnium.model.TimeTable;
import com.learnium.service.TimeTableService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
public class TimeTableController {

    private static final Logger logger = LoggerFactory.getLogger(TimeTableController.class);

    @Autowired
    private TimeTableService service;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public TimeTable createTimeTable(@Valid @RequestBody TimeTable timeTable){
        try {
            TimeTable createdTimeTable = service.addTimeTable(timeTable);
            logger.info("TimeTable created successfully");
            return createdTimeTable;
        } catch (Exception e) {
            logger.error("Error occurred while creating timetable", e);
            throw e;
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER') or hasAuthority('ROLE_FACULTY')")
    public List<TimeTable> getTimeTables() {
        try {
            return service.findAllTimeTables();
        } catch (Exception e) {
            logger.error("Error occurred while fetching all timetables", e);
            throw e;
        }
    }

    @GetMapping("/{timeTableId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER') or hasAuthority('ROLE_FACULTY')")
    public TimeTable getTimeTable(@PathVariable String timeTableId){
        try {
            return service.getTimeTableByTimeTableId(timeTableId);
        } catch (Exception e) {
            logger.error("Error occurred while fetching timetable", e);
            throw e;
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public TimeTable modifyTimeTable(@Valid @RequestBody TimeTable timeTable){
        try {
            return service.updateTimeTable(timeTable);
        } catch (Exception e) {
            logger.error("Error occurred while updating timetable", e);
            throw e;
        }
    }

    @DeleteMapping("/delete/{timeTableId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteTimeTable(@PathVariable String timeTableId){
        try {
            return service.deleteTimeTable(timeTableId);
        } catch (Exception e) {
            logger.error("Error occurred while deleting timetable", e);
            throw e;
        }
    }
}