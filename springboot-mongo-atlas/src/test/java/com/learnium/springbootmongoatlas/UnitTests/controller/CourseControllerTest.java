package com.learnium.springbootmongoatlas.UnitTests.controller;

import com.learnium.model.Course;
import com.learnium.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @BeforeEach
    public void setup() {
        Course course = new Course();
        course.setCourseCode("MOD-001");
        when(courseService.addCourse(any(Course.class))).thenReturn(course);
        when(courseService.findAllCourses()).thenReturn(Collections.singletonList(course));
        when(courseService.getCourseByCourseId(any(String.class))).thenReturn(course);
        when(courseService.updateCourse(any(Course.class))).thenReturn(course);
        when(courseService.deleteCourse(any(String.class))).thenReturn("MOD-001 course deleted from dashboard");
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateCourse() throws Exception {
        mockMvc.perform(post("/api/courses/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"courseCode\":\"MOD-001\", \"courseName\":\"Course Name\", \"credits\":\"3\", \"description\":\"Course Description\", \"facultyId\":\"FAC-001\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetCourses() throws Exception {
        mockMvc.perform(get("/api/courses/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetCourse() throws Exception {
        mockMvc.perform(get("/api/courses/{courseId}", "MOD-001"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testModifyCourse() throws Exception {
        mockMvc.perform(put("/api/courses/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"courseCode\":\"MOD-001\", \"courseName\":\"Updated Course Name\", \"credits\":\"3\", \"description\":\"Updated Course Description\", \"facultyId\":\"FAC-001\"}"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteCourse() throws Exception {
        mockMvc.perform(delete("/api/courses/delete/{courseId}", "MOD-001"))
                .andExpect(status().isOk());
    }

    // Negative test cases
    @Test
    public void testCreateCourseNegative() throws Exception {
        when(courseService.addCourse(any(Course.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/api/courses/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"courseCode\":\"MOD-001\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetCourseNegative() throws Exception {
        when(courseService.getCourseByCourseId(any(String.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(get("/api/courses/{courseId}", "MOD-001"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testModifyCourseNegative() throws Exception {
        when(courseService.updateCourse(any(Course.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(put("/api/courses/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"courseCode\":\"MOD-001\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testDeleteCourseNegative() throws Exception {
        when(courseService.deleteCourse(any(String.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(delete("/api/courses/delete/{courseId}", "MOD-001"))
                .andExpect(status().is4xxClientError());
    }
}