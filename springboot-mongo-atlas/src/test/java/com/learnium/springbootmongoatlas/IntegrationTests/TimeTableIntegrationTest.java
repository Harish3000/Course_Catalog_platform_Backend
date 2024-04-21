package com.learnium.springbootmongoatlas.IntegrationTests;

import com.learnium.model.*;
import com.learnium.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
public class TimeTableIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TimeTableService timeTableService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private ResourceService resourceService;

    LocalDate uniqueDate = LocalDate.now().plusDays(ThreadLocalRandom.current().nextLong(1, 365));

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddTimeTable() throws Exception {
        // Create a mock Faculty
        Faculty faculty = new Faculty("faculty1", "Test Faculty", "testfaculty@example.com", "Professor", "Computer Science", Arrays.asList("CS101"));
        Faculty savedFaculty = facultyService.addFaculty(faculty);

        // Create a mock Course
        Course course = new Course("CS101", "Computer Science", "3", "Introduction to Computer Science", savedFaculty.getFacultyId());
        Course savedCourse = courseService.addCourse(course);

        // Create a mock Room without a reservedDate
        Room room = new Room("room1", "101", "Building 1", null);
        Room savedRoom = roomService.addRoom(room);

        // Create a mock Resource
        Resource resource = new Resource("resource1", "Projector", "Available", null);
        Resource savedResource = resourceService.addResource(resource);

        // Create a TimeTable object
        TimeTable timeTable = new TimeTable();
        timeTable.setCourseCode(savedCourse.getCourseCode());
        timeTable.setRoomId(savedRoom.getRoomId());
        timeTable.setFacultyId(savedFaculty.getFacultyId());
        timeTable.setResourceIds(Arrays.asList(savedResource.getResourceId()));
        timeTable.setDate(uniqueDate);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/timetable/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(timeTable)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFindAllTimeTables() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/timetable/all"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateTimeTable() throws Exception {
        // Create a mock Faculty
        Faculty faculty = new Faculty("faculty2", "Test Faculty 2", "testfaculty2@example.com", "Professor", "Computer Science", Arrays.asList("CS102"));
        Faculty savedFaculty = facultyService.addFaculty(faculty);

        // Create a mock Course
        Course course = new Course("CS102", "Computer Science 2", "3", "Introduction to Computer Science 2", savedFaculty.getFacultyId());
        Course savedCourse = courseService.addCourse(course);

        // Create a mock Room without a reservedDate
        Room room = new Room("room2", "102", "Building 2", null);
        Room savedRoom = roomService.addRoom(room);

        // Create a mock Resource
        Resource resource = new Resource("resource2", "Projector 2", "Available", null);
        Resource savedResource = resourceService.addResource(resource);

        // Create a TimeTable object
        TimeTable timeTable = new TimeTable();
        timeTable.setCourseCode(savedCourse.getCourseCode());
        timeTable.setRoomId(savedRoom.getRoomId());
        timeTable.setFacultyId(savedFaculty.getFacultyId());
        timeTable.setResourceIds(Arrays.asList(savedResource.getResourceId()));
        timeTable.setDate(uniqueDate);

        TimeTable savedTimeTable = timeTableService.addTimeTable(timeTable);

        // Update the TimeTable object
        savedTimeTable.setCourseCode("CS103");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/timetable/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedTimeTable)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteTimeTable() throws Exception {
        // Create a mock Faculty
        Faculty faculty = new Faculty("faculty3", "Test Faculty 3", "testfaculty3@example.com", "Professor", "Computer Science", Arrays.asList("CS103"));
        Faculty savedFaculty = facultyService.addFaculty(faculty);

        // Create a mock Course
        Course course = new Course("CS103", "Computer Science 3", "3", "Introduction to Computer Science 3", savedFaculty.getFacultyId());
        Course savedCourse = courseService.addCourse(course);

        // Create a mock Room without a reservedDate
        Room room = new Room("room3", "103", "Building 3", null);
        Room savedRoom = roomService.addRoom(room);

        // Create a mock Resource
        Resource resource = new Resource("resource3", "Projector 3", "Available", null);
        Resource savedResource = resourceService.addResource(resource);

        // Create a TimeTable object
        TimeTable timeTable = new TimeTable();
        timeTable.setCourseCode(savedCourse.getCourseCode());
        timeTable.setRoomId(savedRoom.getRoomId());
        timeTable.setFacultyId(savedFaculty.getFacultyId());
        timeTable.setResourceIds(Arrays.asList(savedResource.getResourceId()));
        timeTable.setDate(uniqueDate);

        TimeTable savedTimeTable = timeTableService.addTimeTable(timeTable);

        // Delete the TimeTable object
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/timetable/delete/" + savedTimeTable.getTimeTableId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}