package com.learnium.springbootmongoatlas.UnitTests.controller;

import com.learnium.model.Faculty;
import com.learnium.service.FacultyService;
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
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @BeforeEach
    public void setup() {
        Faculty faculty = new Faculty();
        faculty.setFacultyId("FAC-001");
        when(facultyService.addFaculty(any(Faculty.class))).thenReturn(faculty);
        when(facultyService.findAllFacultys()).thenReturn(Collections.singletonList(faculty));
        when(facultyService.getFacultyByFacultyId(any(String.class))).thenReturn(faculty);
        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(faculty);
        when(facultyService.deleteFaculty(any(String.class))).thenReturn("FAC-001 faculty deleted from dashboard");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateFaculty() throws Exception {
        mockMvc.perform(post("/api/faculty/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"facultyId\":\"FAC-001\", \"facultyName\":\"Faculty Name\", \"email\":\"faculty@example.com\", \"designation\":\"Professor\", \"department\":\"Department Name\", \"courseCode\":[]}"))
                .andExpect(status().isCreated());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetFacultys() throws Exception {
        mockMvc.perform(get("/api/faculty/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetFaculty() throws Exception {
        mockMvc.perform(get("/api/faculty/{facultyId}", "FAC-001"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testModifyFaculty() throws Exception {
        mockMvc.perform(put("/api/faculty/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"facultyId\":\"FAC-001\", \"facultyName\":\"Updated Faculty Name\", \"email\":\"updatedfaculty@example.com\", \"designation\":\"Updated Designation\", \"department\":\"Updated Department Name\", \"courseCode\":[]}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteFaculty() throws Exception {
        mockMvc.perform(delete("/api/faculty/delete/{facultyId}", "FAC-001"))
                .andExpect(status().isOk());
    }

    // Negative test cases
    @Test
    public void testCreateFacultyNegative() throws Exception {
        when(facultyService.addFaculty(any(Faculty.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/api/faculty/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"facultyId\":\"FAC-001\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetFacultyNegative() throws Exception {
        when(facultyService.getFacultyByFacultyId(any(String.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(get("/api/faculty/{facultyId}", "FAC-001"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testModifyFacultyNegative() throws Exception {
        when(facultyService.updateFaculty(any(Faculty.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(put("/api/faculty/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"facultyId\":\"FAC-001\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testDeleteFacultyNegative() throws Exception {
        when(facultyService.deleteFaculty(any(String.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(delete("/api/faculty/delete/{facultyId}", "FAC-001"))
                .andExpect(status().is4xxClientError());
    }
}