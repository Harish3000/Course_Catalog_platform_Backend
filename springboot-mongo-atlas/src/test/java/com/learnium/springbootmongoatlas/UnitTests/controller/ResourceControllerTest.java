package com.learnium.springbootmongoatlas.UnitTests.controller;

import com.learnium.model.Resource;
import com.learnium.service.ResourceService;
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
public class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResourceService resourceService;

    @BeforeEach
    public void setup() {
        Resource resource = new Resource();
        resource.setResourceId("RES-001");
        when(resourceService.addResource(any(Resource.class))).thenReturn(resource);
        when(resourceService.findAllResource()).thenReturn(Collections.singletonList(resource));
        when(resourceService.getResourceByResourceId(any(String.class))).thenReturn(resource);
        when(resourceService.updateResource(any(Resource.class))).thenReturn(resource);
        when(resourceService.deleteResource(any(String.class))).thenReturn("RES-001 resource deleted");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateResource() throws Exception {
        mockMvc.perform(post("/api/resource/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"resourceId\":\"RES-001\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetResources() throws Exception {
        mockMvc.perform(get("/api/resource/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetResource() throws Exception {
        mockMvc.perform(get("/api/resource/{resourceId}", "RES-001"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testModifyResource() throws Exception {
        mockMvc.perform(put("/api/resource/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"resourceId\":\"RES-001\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteResource() throws Exception {
        mockMvc.perform(delete("/api/resource/delete/{resourceId}", "RES-001"))
                .andExpect(status().isOk());
    }

    // Negative test cases
    @Test
    public void testCreateResourceNegative() throws Exception {
        when(resourceService.addResource(any(Resource.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/api/resource/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"resourceId\":\"RES-001\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetResourceNegative() throws Exception {
        when(resourceService.getResourceByResourceId(any(String.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(get("/api/resource/{resourceId}", "RES-001"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testModifyResourceNegative() throws Exception {
        when(resourceService.updateResource(any(Resource.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(put("/api/resource/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"resourceId\":\"RES-001\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testDeleteResourceNegative() throws Exception {
        when(resourceService.deleteResource(any(String.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(delete("/api/resource/delete/{resourceId}", "RES-001"))
                .andExpect(status().is4xxClientError());
    }
}