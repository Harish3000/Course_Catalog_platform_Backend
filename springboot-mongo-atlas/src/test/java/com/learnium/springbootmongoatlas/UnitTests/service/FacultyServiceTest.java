package com.learnium.springbootmongoatlas.UnitTests.service;

import com.learnium.model.Faculty;
import com.learnium.model.UserInfo;
import com.learnium.repository.FacultyRepository;
import com.learnium.service.FacultyService;
import com.learnium.service.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FacultyServiceTest {

    @Mock
    private FacultyRepository repository;

    @InjectMocks
    private FacultyService facultyService;

    @Mock
    private UserInfoService userInfoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddFaculty() {
        Faculty faculty = new Faculty();
        faculty.setFacultyId("faculty1");
        when(repository.save(faculty)).thenReturn(faculty);
        when(userInfoService.addUser(any())).thenReturn("User added successfully");
        Faculty result = facultyService.addFaculty(faculty);
        assertEquals(faculty, result);
    }

    @Test
    public void testGetFacultyByFacultyId() {
        String facultyId = "faculty1";
        Faculty faculty = new Faculty();
        when(repository.findById(facultyId)).thenReturn(Optional.of(faculty));
        Faculty result = facultyService.getFacultyByFacultyId(facultyId);
        assertEquals(faculty, result);
    }

    @Test
    public void testGetFacultyByFacultyIdNotFound() {
        String facultyId = "faculty1";
        when(repository.findById(facultyId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> facultyService.getFacultyByFacultyId(facultyId));
    }

    @Test
    public void testUpdateFaculty() {
        Faculty facultyRequest = new Faculty();
        facultyRequest.setFacultyId("faculty1");
        Faculty existingFaculty = new Faculty();
        existingFaculty.setFacultyId("faculty1");
        when(repository.findById("faculty1")).thenReturn(Optional.of(existingFaculty));
        when(repository.save(any(Faculty.class))).thenReturn(facultyRequest);

        // Mock the behavior of getUserByUsername method
        UserInfo existingUser = new UserInfo();
        existingUser.setUserName("faculty1");
        when(userInfoService.getUserByUsername(anyString())).thenReturn(existingUser);

        Faculty result = facultyService.updateFaculty(facultyRequest);
        assertEquals(facultyRequest, result);
    }

    @Test
    public void testDeleteFaculty() {
        String facultyId = "faculty1";
        Faculty faculty = new Faculty();
        faculty.setFacultyId("faculty1");
        when(repository.findById(facultyId)).thenReturn(Optional.of(faculty));

        // Mock the behavior of getUserByUsername method
        UserInfo existingUser = new UserInfo();
        existingUser.setId("user1");
        when(userInfoService.getUserByUsername(faculty.getFacultyName() + "-" + faculty.getFacultyId())).thenReturn(existingUser);

        String result = facultyService.deleteFaculty(facultyId);
        assertEquals(facultyId + " faculty and corresponding user deleted successfully", result.trim());
    }
    @Test
    public void testFindAllFaculties() {
        List<Faculty> faculties = new ArrayList<>();
        when(repository.findAll()).thenReturn(faculties);
        List<Faculty> result = facultyService.findAllFacultys();
        assertEquals(faculties, result);
    }
}