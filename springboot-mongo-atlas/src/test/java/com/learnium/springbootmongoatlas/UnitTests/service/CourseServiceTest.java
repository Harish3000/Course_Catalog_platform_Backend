package com.learnium.springbootmongoatlas.UnitTests.service;

import com.learnium.model.Course;
import com.learnium.model.Faculty;
import com.learnium.model.UserInfo;
import com.learnium.repository.CourseRepository;
import com.learnium.service.CourseService;
import com.learnium.service.EnrollmentService;
import com.learnium.service.FacultyService;
import com.learnium.service.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;





public class CourseServiceTest {

    @Mock
    private CourseRepository repository;
    @Mock
    private FacultyService facultyService;
    @Mock
    private UserInfoService userInfoService;
    @Mock
    private EnrollmentService enrollmentService;
    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddCourse() {
        Course course = new Course();
        course.setFacultyId("faculty1");
        when(facultyService.doesFacultyExist(course.getFacultyId())).thenReturn(true);
        when(repository.findAll(Sort.by(Sort.Direction.DESC, "courseCode"))).thenReturn(new ArrayList<>());
        when(repository.save(course)).thenReturn(course);
        Faculty faculty = new Faculty();
        faculty.setCourseCode(new ArrayList<>());
        when(facultyService.getFacultyByFacultyId(course.getFacultyId())).thenReturn(faculty);
        Course result = courseService.addCourse(course);
        assertEquals(course, result);
    }

    @Test
    public void testAddCourseInvalidFaculty() {
        Course course = new Course();
        course.setFacultyId("faculty1");
        when(facultyService.doesFacultyExist(course.getFacultyId())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> courseService.addCourse(course));
    }

    @Test
    public void testFindAllCourses() {
        List<Course> courses = new ArrayList<>();
        when(repository.findAll()).thenReturn(courses);
        List<Course> result = courseService.findAllCourses();
        assertEquals(courses, result);
    }

    @Test
    public void testGetCourseByCourseId() {
        // Set up the SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Set up the UserDetails
        userDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String courseId = "course1";
        Course course = new Course();
        when(repository.findById(courseId)).thenReturn(Optional.of(course));
        when(userDetails.getUsername()).thenReturn("user1");
        UserInfo userInfo = new UserInfo();
        userInfo.setId("student1"); // Set the studentId
        when(userInfoService.getUserByUsername("user1")).thenReturn(userInfo);
        when(enrollmentService.isStudentEnrolledInCourse("student1", courseId)).thenReturn(true);
        Course result = courseService.getCourseByCourseId(courseId);
        assertEquals(course, result);
    }

    @Test
    public void testGetCourseByCourseIdNotEnrolled() {
        // Set up the SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Set up the UserDetails
        userDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String courseId = "course1";
        when(userDetails.getUsername()).thenReturn("user1");
        UserInfo userInfo = new UserInfo();
        userInfo.setId("student1"); // Set the studentId
        when(userInfoService.getUserByUsername("user1")).thenReturn(userInfo);
        when(enrollmentService.isStudentEnrolledInCourse("student1", courseId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> courseService.getCourseByCourseId(courseId));
    }

    @Test
    public void testUpdateCourse() {
        Course courseRequest = new Course();
        courseRequest.setCourseCode("course1");
        courseRequest.setFacultyId("faculty2");
        Course existingCourse = new Course();
        existingCourse.setCourseCode("course1");
        existingCourse.setFacultyId("faculty1");
        when(repository.findById("course1")).thenReturn(Optional.of(existingCourse));
        Faculty currentFaculty = new Faculty();
        currentFaculty.setCourseCode(new ArrayList<>());
        when(facultyService.getFacultyByFacultyId("faculty1")).thenReturn(currentFaculty);
        Faculty newFaculty = new Faculty();
        newFaculty.setCourseCode(new ArrayList<>());
        when(facultyService.getFacultyByFacultyId("faculty2")).thenReturn(newFaculty);
        when(repository.save(any(Course.class))).thenReturn(courseRequest);
        Course result = courseService.updateCourse(courseRequest);
        assertEquals(courseRequest, result);
    }


    @Test
    public void testDeleteCourse() {
        String courseId = "course1";
        Course course = new Course();
        course.setCourseCode("course1");
        course.setFacultyId("faculty1");
        when(repository.findById(courseId)).thenReturn(Optional.of(course));
        Faculty faculty = new Faculty();
        faculty.setCourseCode(new ArrayList<>());
        when(facultyService.getFacultyByFacultyId("faculty1")).thenReturn(faculty);
        String result = courseService.deleteCourse(courseId);
        assertEquals(courseId + " course deleted from dashboard ", result);
    }

}