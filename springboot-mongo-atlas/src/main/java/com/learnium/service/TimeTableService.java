package com.learnium.service;

import com.learnium.model.Resource;
import com.learnium.model.Room;
import com.learnium.model.TimeTable;
import com.learnium.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TimeTableService {

    @Autowired
    private TimeTableRepository timeTableRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private EmailService emailService;


    //create
    public TimeTable addTimeTable(TimeTable timeTable) {

        // Check if course code is valid
        if (!courseRepository.existsByCourseCode(timeTable.getCourseCode())) {
            throw new IllegalArgumentException("Invalid course code");
        }

        // Check if roomId is valid
        if (!roomRepository.existsById(timeTable.getRoomId())) {
            throw new IllegalArgumentException("Invalid room id");
        }

        // Check if facultyId is valid
        if (!facultyRepository.existsById(timeTable.getFacultyId())) {
            throw new IllegalArgumentException("Invalid faculty id");
        }

        // Check if resourceIds are valid
        for (String resourceId : timeTable.getResourceIds()) {
            if (!resourceRepository.existsById(resourceId)) {
                throw new IllegalArgumentException("Invalid resource id: " + resourceId);
            }
        }

        // Check if timetable entry already exists for this course code
        if (timeTableRepository.existsByCourseCode(timeTable.getCourseCode())) {
            throw new IllegalArgumentException("Timetable entry already exists for this course code");
        }

        // Check if room is already reserved for the given date
        if (roomRepository.existsByRoomIdAndReservedDate(timeTable.getRoomId(), timeTable.getDate())) {
            throw new IllegalArgumentException("Room is already reserved for the given date");
        }

        // Check if resources are already reserved for the given date
        for (String resourceId : timeTable.getResourceIds()) {
            if (resourceRepository.existsByResourceIdAndReservedDate(resourceId, timeTable.getDate())) {
                throw new IllegalArgumentException("Resource " + resourceId + " is already reserved for the given date");
            }
        }

        // Update the reservedDate in the Room document
        Room room = roomRepository.findById(timeTable.getRoomId()).orElseThrow(() -> new IllegalArgumentException("Invalid room ids"));
        room.setReservedDate(timeTable.getDate());
        roomRepository.save(room);

        // Update the reservedDate in the Resource documents
        for (String resourceId : timeTable.getResourceIds()) {
            Resource resource = resourceRepository.findById(resourceId).orElseThrow(() -> new IllegalArgumentException("Invalid resource id: " + resourceId));
            resource.setReservedDate(timeTable.getDate());
            resourceRepository.save(resource);
        }

        List<TimeTable> tables = timeTableRepository.findAll(Sort.by(Sort.Direction.DESC, "timeTableId"));

        if (!tables.isEmpty()) {
            String lastTimeTableId = tables.get(0).getTimeTableId();
            String numericPart = lastTimeTableId.substring(4);
            int newId = Integer.parseInt(numericPart) + 1;
            String newTimeTableId = String.format("TAB-%03d", newId);
            timeTable.setTimeTableId(newTimeTableId);
        } else {
            timeTable.setTimeTableId("TAB-001");
        }
        return timeTableRepository.save(timeTable);
    }

    //read all
    public List<TimeTable> findAllTimeTables() {
        return timeTableRepository.findAll();
    }

    //read one
    public TimeTable getTimeTableByTimeTableId(String timeTableId) {
        return timeTableRepository.findById(timeTableId).get();
    }

    public TimeTable updateTimeTable(TimeTable timeTableRequest) {
        //get the existing document from DB
        // populate new value from request to existing object/entity/document
        TimeTable existingTimeTable = timeTableRepository.findById(timeTableRequest.getTimeTableId()).get();
        existingTimeTable.setType(timeTableRequest.getType());
        existingTimeTable.setCourseCode(timeTableRequest.getCourseCode());
        existingTimeTable.setRoomId(timeTableRequest.getRoomId());
        existingTimeTable.setResourceIds(timeTableRequest.getResourceIds());
        existingTimeTable.setFacultyId(timeTableRequest.getFacultyId());

        emailService.sendEmail(existingTimeTable);
        return timeTableRepository.save(existingTimeTable);
    }

    public String deleteTimeTable(String timeTableId) {
        timeTableRepository.deleteById(timeTableId);
        return timeTableId + " timeTable deleted from dashboard ";
    }


    public boolean doesTimeTableExist(String timeTableId) {
        return timeTableRepository.existsById(timeTableId);
    }

}
