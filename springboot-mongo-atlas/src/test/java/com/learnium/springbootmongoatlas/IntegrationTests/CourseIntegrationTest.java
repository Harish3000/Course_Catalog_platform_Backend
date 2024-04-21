package com.learnium.springbootmongoatlas.IntegrationTests;

import com.learnium.model.Course;
import com.learnium.model.Faculty;
import com.learnium.service.CourseService;
import com.learnium.service.FacultyService;
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

import java.util.Collections;

@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
public class CourseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FacultyService facultyService;
    @Autowired
    private CourseService courseService;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateCourse() throws Exception {
        // Create a mock Faculty
        Faculty faculty = new Faculty();
        faculty.setFacultyName("Test Faculty");
        faculty.setEmail("testfaculty@example.com");
        faculty.setDesignation("Professor");
        faculty.setDepartment("Computer Science");
        faculty.setCourseCode(Collections.singletonList("CS101"));
        Faculty savedFaculty = facultyService.addFaculty(faculty);

        // Use the facultyId of the saved Faculty in the Course
        Course course = new Course("", "Computer Science", "3", "Introduction to Computer Science", savedFaculty.getFacultyId());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/courses/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetCourses() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/all"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateCourse() throws Exception {
        // Create a mock Faculty
        Faculty faculty = new Faculty();
        faculty.setFacultyName("Test Faculty");
        faculty.setEmail("testfaculty@example.com");
        faculty.setDesignation("Professor");
        faculty.setDepartment("Computer Science");
        faculty.setCourseCode(Collections.singletonList("CS101"));
        Faculty savedFaculty = facultyService.addFaculty(faculty);

        // Use the facultyId of the saved Faculty in the Course
        Course course = new Course("CS101", "Computer Science", "3", "Introduction to Computer Science", savedFaculty.getFacultyId());
        Course savedCourse = courseService.addCourse(course);

        // Update the course
        savedCourse.setCourseName("Updated Course Name");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/courses/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedCourse)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteCourse() throws Exception {
        // Create a mock Faculty
        Faculty faculty = new Faculty();
        faculty.setFacultyName("Test Faculty");
        faculty.setEmail("testfaculty@example.com");
        faculty.setDesignation("Professor");
        faculty.setDepartment("Computer Science");
        faculty.setCourseCode(Collections.singletonList("CS101"));
        Faculty savedFaculty = facultyService.addFaculty(faculty);

        // Use the facultyId of the saved Faculty in the Course
        Course course = new Course("CS101", "Computer Science", "3", "Introduction to Computer Science", savedFaculty.getFacultyId());
        Course savedCourse = courseService.addCourse(course);

        // Delete the course
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/courses/delete/" + savedCourse.getCourseCode()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}