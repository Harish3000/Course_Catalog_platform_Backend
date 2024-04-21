package com.learnium.springbootmongoatlas.UnitTests.service;

import com.learnium.model.UserInfo;
import com.learnium.repository.UserInfoRepository;
import com.learnium.service.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserInfoServiceTest {

    @Mock
    private UserInfoRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserInfoService userInfoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUser() {
        UserInfo userInfo = new UserInfo("USER-001", "username", "name", "email", "password", "roles");
        when(repository.findAll(Sort.by(Sort.Direction.DESC, "id"))).thenReturn(new ArrayList<>());
        when(passwordEncoder.encode(userInfo.getPassword())).thenReturn("encodedPassword");
        when(repository.save(userInfo)).thenReturn(userInfo);
        String result = userInfoService.addUser(userInfo);
        assertEquals("User added successfully", result);
    }

    @Test
    public void testGetUserByUsername() {
        String username = "username";
        UserInfo userInfo = new UserInfo("USER-001", "username", "name", "email", "password", "roles");
        when(repository.findByUserName(username)).thenReturn(Optional.of(userInfo));
        UserInfo result = userInfoService.getUserByUsername(username);
        assertEquals(userInfo, result);
    }

    @Test
    public void testGetUserByUsernameNotFound() {
        String username = "username";
        when(repository.findByUserName(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userInfoService.getUserByUsername(username));
    }

    @Test
    public void testGetAllUsers() {
        List<UserInfo> users = new ArrayList<>();
        when(repository.findAll()).thenReturn(users);
        List<UserInfo> result = userInfoService.getAllUsers();
        assertEquals(users, result);
    }

    @Test
    public void testUpdateUser() {
        UserInfo userInfoRequest = new UserInfo("USER-001", "username", "name", "email", "password", "roles");
        UserInfo existingUser = new UserInfo("USER-001", "username2", "name2", "email2", "password2", "roles2");
        when(repository.findById("USER-001")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(userInfoRequest.getPassword())).thenReturn("encodedPassword");
        when(repository.save(any(UserInfo.class))).thenReturn(userInfoRequest);
        UserInfo result = userInfoService.updateUser(userInfoRequest);
        assertEquals(userInfoRequest, result);
    }

    @Test
    public void testDeleteUser() {
        String userId = "USER-001";
        UserInfo user = new UserInfo("USER-001", "username", "name", "email", "password", "roles");
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(repository).deleteById(userId);
        String result = userInfoService.deleteUser(userId);
        assertEquals(userId + " user deleted successfully", result);
    }

    @Test
    public void testDeleteUserNotFound() {
        String userId = "USER-001";
        when(repository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userInfoService.deleteUser(userId));
    }
}