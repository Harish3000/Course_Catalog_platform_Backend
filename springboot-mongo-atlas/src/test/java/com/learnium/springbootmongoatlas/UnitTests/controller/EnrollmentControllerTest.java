package com.learnium.springbootmongoatlas.UnitTests.controller;

import com.learnium.model.Enrollment;
import com.learnium.service.EnrollmentService;
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
public class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentService enrollmentService;

    @BeforeEach
    public void setup() {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId("ENR-001");
        when(enrollmentService.addEnrollment(any(Enrollment.class))).thenReturn(enrollment);
        when(enrollmentService.findAllEnrollments()).thenReturn(Collections.singletonList(enrollment));
        when(enrollmentService.getEnrollmentByEnrollmentId(any(String.class))).thenReturn(enrollment);
        when(enrollmentService.updateEnrollment(any(Enrollment.class))).thenReturn(enrollment);
        when(enrollmentService.deleteEnrollment(any(String.class))).thenReturn("ENR-001 enrollment deleted");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateEnrollment() throws Exception {
        mockMvc.perform(post("/api/enrollment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"enrollmentId\":\"ENR-001\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetEnrollments() throws Exception {
        mockMvc.perform(get("/api/enrollment/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetEnrollment() throws Exception {
        mockMvc.perform(get("/api/enrollment/{enrollmentId}", "ENR-001"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testModifyEnrollment() throws Exception {
        mockMvc.perform(put("/api/enrollment/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"enrollmentId\":\"ENR-001\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteEnrollment() throws Exception {
        mockMvc.perform(delete("/api/enrollment/delete/{enrollmentId}", "ENR-001"))
                .andExpect(status().isOk());
    }

    // Negative test cases
    @Test
    public void testCreateEnrollmentNegative() throws Exception {
        when(enrollmentService.addEnrollment(any(Enrollment.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/api/enrollment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"enrollmentId\":\"ENR-001\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetEnrollmentNegative() throws Exception {
        when(enrollmentService.getEnrollmentByEnrollmentId(any(String.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(get("/api/enrollment/{enrollmentId}", "ENR-001"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testModifyEnrollmentNegative() throws Exception {
        when(enrollmentService.updateEnrollment(any(Enrollment.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(put("/api/enrollment/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"enrollmentId\":\"ENR-001\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testDeleteEnrollmentNegative() throws Exception {
        when(enrollmentService.deleteEnrollment(any(String.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(delete("/api/enrollment/delete/{enrollmentId}", "ENR-001"))
                .andExpect(status().is4xxClientError());
    }
}