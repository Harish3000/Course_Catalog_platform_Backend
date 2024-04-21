package com.learnium.springbootmongoatlas.IntegrationTests;

import com.learnium.model.UserInfo;
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

@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
public class UserInfoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserInfoService userInfoService;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddUser() throws Exception {
        UserInfo userInfo = new UserInfo("USER-001", "username1", "Test User", "testuser@example.com", "testPassword", "ROLE_USER");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInfo)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/all"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateUser() throws Exception {
        // Generate a unique username using the current timestamp
        String uniqueUsername = "username" + System.currentTimeMillis();

        UserInfo userInfo = new UserInfo("USER-001", uniqueUsername, "Test User", "testuser@example.com", "testPassword", "ROLE_USER");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInfo)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Update the user
        userInfo.setUserName("username2");
        userInfo.setName("name2");
        userInfo.setEmail("email2@gmail.com");
        userInfo.setPassword("password2");
        userInfo.setRoles("roles2");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInfo)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteUser() throws Exception {
        // Generate a unique username using the current timestamp
        String uniqueUsername = "username" + System.currentTimeMillis();

        UserInfo userInfo = new UserInfo("USER-001", uniqueUsername, "Test User", "testuser@example.com", "testPassword", "ROLE_USER");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInfo)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/delete/" + userInfo.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}