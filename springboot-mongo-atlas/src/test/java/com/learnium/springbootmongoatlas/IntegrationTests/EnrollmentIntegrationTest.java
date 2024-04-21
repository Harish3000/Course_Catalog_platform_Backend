package com.learnium.springbootmongoatlas.IntegrationTests;

import com.learnium.model.Course;
import com.learnium.model.Enrollment;
import com.learnium.model.Faculty;
import com.learnium.model.UserInfo;
import com.learnium.service.CourseService;
import com.learnium.service.EnrollmentService;
import com.learnium.service.FacultyService;
import com.learnium.service.UserInfoService;
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

import java.util.Arrays;
import java.util.Collections;

@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
public class EnrollmentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private FacultyService facultyService;


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddEnrollment() throws Exception {
        // Create a mock Faculty
        Faculty faculty = new Faculty();
        faculty.setFacultyName("Test Faculty");
        faculty.setEmail("testfaculty@example.com");
        faculty.setDesignation("Professor");
        faculty.setDepartment("Computer Science");
        faculty.setCourseCode(Collections.singletonList("CS101"));
        Faculty savedFaculty = facultyService.addFaculty(faculty);

        // Create a mock Course
        Course course = new Course("CS101", "Computer Science", "3", "Introduction to Computer Science", savedFaculty.getFacultyId());
        course = courseService.addCourse(course);

        // Create mock Users
        UserInfo user1 = new UserInfo("USER-001", "username1", "name1", "email1", "password1", "roles1");
        UserInfo user2 = new UserInfo("USER-002", "username2", "name2", "email2", "password2", "roles2");
        userInfoService.addUser(user1);
        userInfoService.addUser(user2);

        Enrollment enrollment = new Enrollment("ENR-001", course.getCourseCode(), Arrays.asList(user1.getId(), user2.getId()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/enrollment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollment)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFindAllEnrollments() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/enrollment/all"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateEnrollment() throws Exception {
        // Create a mock Course
        Course course = new Course("CS101", "Computer Science", "3", "Introduction to Computer Science", "FAC-001");
        course = courseService.addCourse(course);

        // Create mock User
        UserInfo user = new UserInfo("USER-001", "username1", "name1", "email1", "password1", "roles1");
        userInfoService.addUser(user);

        // Create an Enrollment
        Enrollment enrollment = new Enrollment("ENR-001", course.getCourseCode(), Arrays.asList(user.getId()));
        Enrollment savedEnrollment = enrollmentService.addEnrollment(enrollment);

        // Update the saved Enrollment
        savedEnrollment.setCourseCode("CS102");
        savedEnrollment.setStudentId(Arrays.asList("S3", "S4"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/enrollment/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedEnrollment)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteEnrollment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/enrollment/delete/ENR-001"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}