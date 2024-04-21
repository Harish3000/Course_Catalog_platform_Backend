package com.learnium.springbootmongoatlas.UnitTests.controller;

import com.learnium.model.AuthRequest;
import com.learnium.model.UserInfo;
import com.learnium.service.JwtService;
import com.learnium.service.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserInfoService userInfoService;

    @BeforeEach
    public void setup() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("testUser");
        Authentication auth = new UsernamePasswordAuthenticationToken("testUser", "testPassword");
        when(userInfoService.addUser(any(UserInfo.class))).thenReturn(String.valueOf(userInfo));
        when(userInfoService.getAllUsers()).thenReturn(Collections.singletonList(userInfo));
        when(userInfoService.updateUser(any(UserInfo.class))).thenReturn(userInfo);
        when(userInfoService.deleteUser(any(String.class))).thenReturn("User deleted successfully");
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtService.generateToken(any(String.class))).thenReturn("testToken");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddNewUser() throws Exception {
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"testUser\", \"name\":\"Test User\", \"email\":\"testuser@example.com\", \"password\":\"testPassword\", \"roles\":\"ROLE_USER\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthenticateAndGetToken() throws Exception {
        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        mockMvc.perform(post("/api/users/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\", \"password\":\"testPassword\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateUser() throws Exception {
        mockMvc.perform(put("/api/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"testUser\", \"name\":\"Test User\", \"email\":\"testuser@example.com\", \"password\":\"testPassword\", \"roles\":\"ROLE_USER\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/delete/{userId}", "testUser"))
                .andExpect(status().isOk());
    }

    // Negative test cases
    @Test
    public void testAddNewUserNegative() throws Exception {
        when(userInfoService.addUser(any(UserInfo.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\", \"password\":\"testPassword\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testAuthenticateAndGetTokenNegative() throws Exception {
        when(authenticationManager.authenticate(any())).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/api/users/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\", \"password\":\"testPassword\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllUsersNegative() throws Exception {
        when(userInfoService.getAllUsers()).thenThrow(new IllegalArgumentException());
        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateUserNegative() throws Exception {
        when(userInfoService.updateUser(any(UserInfo.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(put("/api/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\", \"password\":\"testPassword\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteUserNegative() throws Exception {
        when(userInfoService.deleteUser(any(String.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(delete("/api/users/delete/{userId}", "testUser"))
                .andExpect(status().is4xxClientError());
    }
}