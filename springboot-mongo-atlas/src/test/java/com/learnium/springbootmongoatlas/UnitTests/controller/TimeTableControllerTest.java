package com.learnium.springbootmongoatlas.UnitTests.controller;

import com.learnium.model.TimeTable;
import com.learnium.service.TimeTableService;
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
public class TimeTableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimeTableService timeTableService;

    @BeforeEach
    public void setup() {
        TimeTable timeTable = new TimeTable();
        timeTable.setTimeTableId("TT-001");
        when(timeTableService.addTimeTable(any(TimeTable.class))).thenReturn(timeTable);
        when(timeTableService.findAllTimeTables()).thenReturn(Collections.singletonList(timeTable));
        when(timeTableService.getTimeTableByTimeTableId(any(String.class))).thenReturn(timeTable);
        when(timeTableService.updateTimeTable(any(TimeTable.class))).thenReturn(timeTable);
        when(timeTableService.deleteTimeTable(any(String.class))).thenReturn("TT-001 timetable deleted");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateTimeTable() throws Exception {
        mockMvc.perform(post("/api/timetable/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"timeTableId\":\"TT-001\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetTimeTables() throws Exception {
        mockMvc.perform(get("/api/timetable/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetTimeTable() throws Exception {
        mockMvc.perform(get("/api/timetable/{timeTableId}", "TT-001"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testModifyTimeTable() throws Exception {
        mockMvc.perform(put("/api/timetable/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"timeTableId\":\"TT-001\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteTimeTable() throws Exception {
        mockMvc.perform(delete("/api/timetable/delete/{timeTableId}", "TT-001"))
                .andExpect(status().isOk());
    }

    // Negative test cases
    @Test
    public void testCreateTimeTableNegative() throws Exception {
        when(timeTableService.addTimeTable(any(TimeTable.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/api/timetable/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"timeTableId\":\"TT-001\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetTimeTableNegative() throws Exception {
        when(timeTableService.getTimeTableByTimeTableId(any(String.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(get("/api/timetable/{timeTableId}", "TT-001"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testModifyTimeTableNegative() throws Exception {
        when(timeTableService.updateTimeTable(any(TimeTable.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(put("/api/timetable/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"timeTableId\":\"TT-001\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testDeleteTimeTableNegative() throws Exception {
        when(timeTableService.deleteTimeTable(any(String.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(delete("/api/timetable/delete/{timeTableId}", "TT-001"))
                .andExpect(status().is4xxClientError());
    }
}