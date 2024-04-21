package com.learnium.springbootmongoatlas.UnitTests.service;

import com.learnium.model.Enrollment;
import com.learnium.repository.CourseRepository;
import com.learnium.repository.EnrollmentRepository;
import com.learnium.repository.UserInfoRepository;
import com.learnium.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EnrollmentServiceTest {

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserInfoRepository userInfoRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddEnrollmentPositive() {
        Enrollment enrollment = new Enrollment();
        enrollment.setCourseCode("CS101");
        enrollment.setStudentId(Arrays.asList("S1", "S2"));

        when(courseRepository.existsByCourseCode("CS101")).thenReturn(true);
        when(userInfoRepository.existsById("S1")).thenReturn(true);
        when(userInfoRepository.existsById("S2")).thenReturn(true);
        when(enrollmentRepository.findAll(Sort.by(Sort.Direction.DESC, "enrollmentId"))).thenReturn(Collections.emptyList());
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment result = enrollmentService.addEnrollment(enrollment);

        assertEquals(enrollment, result);
    }

    @Test
    public void testAddEnrollmentNegative() {
        Enrollment enrollment = new Enrollment();
        enrollment.setCourseCode("CS101");
        enrollment.setStudentId(Arrays.asList("S1", "S2"));

        when(courseRepository.existsByCourseCode("CS101")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> enrollmentService.addEnrollment(enrollment));
    }

    @Test
    public void testFindAllEnrollmentsPositive() {
        List<Enrollment> enrollments = Arrays.asList(new Enrollment(), new Enrollment());
        when(enrollmentRepository.findAll()).thenReturn(enrollments);

        List<Enrollment> result = enrollmentService.findAllEnrollments();

        assertEquals(enrollments, result);
    }

    @Test
    public void testGetEnrollmentByEnrollmentIdPositive() {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId("ENR-001");
        when(enrollmentRepository.findById("ENR-001")).thenReturn(Optional.of(enrollment));

        Enrollment result = enrollmentService.getEnrollmentByEnrollmentId("ENR-001");

        assertEquals(enrollment, result);
    }

    @Test
    public void testGetEnrollmentByEnrollmentIdNegative() {
        String enrollmentId = "ENR-001";
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> enrollmentService.getEnrollmentByEnrollmentId(enrollmentId));
        assertEquals("Invalid enrollment id: " + enrollmentId, exception.getMessage());
    }

    @Test
    public void testUpdateEnrollmentPositive() {
        Enrollment existingEnrollment = new Enrollment();
        existingEnrollment.setEnrollmentId("ENR-001");
        existingEnrollment.setCourseCode("CS101");
        existingEnrollment.setStudentId(Arrays.asList("S1", "S2"));

        Enrollment updatedEnrollment = new Enrollment();
        updatedEnrollment.setEnrollmentId("ENR-001");
        updatedEnrollment.setCourseCode("CS102");
        updatedEnrollment.setStudentId(Arrays.asList("S3", "S4"));

        when(enrollmentRepository.findById("ENR-001")).thenReturn(Optional.of(existingEnrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(updatedEnrollment);

        Enrollment result = enrollmentService.updateEnrollment(updatedEnrollment);

        assertEquals(updatedEnrollment, result);
    }

    @Test
    public void testUpdateEnrollmentNegative() {
        Enrollment updatedEnrollment = new Enrollment();
        updatedEnrollment.setEnrollmentId("ENR-001");
        updatedEnrollment.setCourseCode("CS102");
        updatedEnrollment.setStudentId(Arrays.asList("S3", "S4"));

        when(enrollmentRepository.findById("ENR-001")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> enrollmentService.updateEnrollment(updatedEnrollment));
        assertEquals("Invalid enrollment id: " + updatedEnrollment.getEnrollmentId(), exception.getMessage());
    }
    @Test
    public void testDeleteEnrollmentPositive() {
        doNothing().when(enrollmentRepository).deleteById("ENR-001");

        String result = enrollmentService.deleteEnrollment("ENR-001");

        assertEquals("ENR-001 enrollment deleted from dashboard ", result);
    }

    @Test
    public void testIsStudentEnrolledInCoursePositive() {
        when(enrollmentRepository.existsByStudentIdAndCourseCode("S1", "CS101")).thenReturn(true);

        boolean result = enrollmentService.isStudentEnrolledInCourse("S1", "CS101");

        assertTrue(result);
    }

    @Test
    public void testIsStudentEnrolledInCourseNegative() {
        when(enrollmentRepository.existsByStudentIdAndCourseCode("S1", "CS101")).thenReturn(false);

        boolean result = enrollmentService.isStudentEnrolledInCourse("S1", "CS101");

        assertFalse(result);
    }



}