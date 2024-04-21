package com.learnium.springbootmongoatlas.UnitTests.service;

import com.learnium.model.*;
import com.learnium.repository.*;
import com.learnium.service.EmailService;
import com.learnium.service.TimeTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TimeTableServiceTest {

    @Mock
    private TimeTableRepository timeTableRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private FacultyRepository facultyRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private TimeTableService timeTableService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddTimeTable() {
        // Create valid course, room, faculty, and resource objects
        Course course = new Course();
        course.setCourseCode("course1");
        when(courseRepository.existsByCourseCode(course.getCourseCode())).thenReturn(true);
        when(courseRepository.save(course)).thenReturn(course);

        Room room = new Room();
        room.setRoomId("room1");
        room.setReservedDate(LocalDate.now()); // Set a valid date
        when(roomRepository.existsById(room.getRoomId())).thenReturn(true);
        when(roomRepository.findById(room.getRoomId())).thenReturn(Optional.of(room));
        when(roomRepository.save(room)).thenReturn(room);

        Faculty faculty = new Faculty();
        faculty.setFacultyId("faculty1");
        when(facultyRepository.existsById(faculty.getFacultyId())).thenReturn(true);
        when(facultyRepository.save(faculty)).thenReturn(faculty);

        Resource resource = new Resource();
        resource.setResourceId("resource1");
        when(resourceRepository.existsById(resource.getResourceId())).thenReturn(true);
        when(resourceRepository.findById(resource.getResourceId())).thenReturn(Optional.of(resource));
        when(resourceRepository.save(resource)).thenReturn(resource);

        // Create TimeTable object
        TimeTable timeTable = new TimeTable();
        timeTable.setCourseCode(course.getCourseCode());
        timeTable.setRoomId(room.getRoomId());
        timeTable.setFacultyId(faculty.getFacultyId());
        timeTable.setResourceIds(Arrays.asList(resource.getResourceId()));
        timeTable.setDate(room.getReservedDate()); // Set the date from the Room object

        when(timeTableRepository.existsByCourseCode(timeTable.getCourseCode())).thenReturn(false);
        when(roomRepository.existsByRoomIdAndReservedDate(timeTable.getRoomId(), timeTable.getDate())).thenReturn(false);
        when(timeTableRepository.findAll()).thenReturn(new ArrayList<>());
        when(timeTableRepository.save(timeTable)).thenReturn(timeTable);

        TimeTable result = timeTableService.addTimeTable(timeTable);
        assertEquals(timeTable, result);
    }

    @Test
    public void testAddTimeTableInvalidCourseCode() {
        TimeTable timeTable = new TimeTable();
        timeTable.setCourseCode("course1");
        when(courseRepository.existsByCourseCode(timeTable.getCourseCode())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> timeTableService.addTimeTable(timeTable));
    }

    @Test
    public void testFindAllTimeTables() {
        List<TimeTable> timeTables = new ArrayList<>();
        when(timeTableRepository.findAll()).thenReturn(timeTables);
        List<TimeTable> result = timeTableService.findAllTimeTables();
        assertEquals(timeTables, result);
    }

    @Test
    public void testGetTimeTableByTimeTableId() {
        String timeTableId = "table1";
        TimeTable timeTable = new TimeTable();
        when(timeTableRepository.findById(timeTableId)).thenReturn(Optional.of(timeTable));
        TimeTable result = timeTableService.getTimeTableByTimeTableId(timeTableId);
        assertEquals(timeTable, result);
    }

    @Test
    public void testUpdateTimeTable() {
        TimeTable timeTableRequest = new TimeTable();
        timeTableRequest.setTimeTableId("table1");
        TimeTable existingTimeTable = new TimeTable();
        when(timeTableRepository.findById(timeTableRequest.getTimeTableId())).thenReturn(Optional.of(existingTimeTable));
        doNothing().when(emailService).sendEmail(any(TimeTable.class));
        when(timeTableRepository.save(any(TimeTable.class))).thenReturn(timeTableRequest);
        TimeTable result = timeTableService.updateTimeTable(timeTableRequest);
        assertEquals(timeTableRequest, result);
    }

    @Test
    public void testDeleteTimeTable() {
        String timeTableId = "table1";
        when(timeTableRepository.existsById(timeTableId)).thenReturn(true);
        String result = timeTableService.deleteTimeTable(timeTableId);
        assertEquals(timeTableId + " timeTable deleted from dashboard ", result);
    }

    @Test
    public void testDoesTimeTableExist() {
        String timeTableId = "table1";
        when(timeTableRepository.existsById(timeTableId)).thenReturn(true);
        boolean result = timeTableService.doesTimeTableExist(timeTableId);
        assertTrue(result);
    }

}