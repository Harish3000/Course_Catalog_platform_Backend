package com.learnium.service;

import com.learnium.model.Course;
import com.learnium.model.Faculty;
import com.learnium.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CourseService {

    @Autowired
    private CourseRepository repository;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private EnrollmentService enrollmentService;


    public Course addCourse(Course course) {
        if (!facultyService.doesFacultyExist(course.getFacultyId())) {
            throw new IllegalArgumentException("Invalid faculty ID");
        }
        List<Course> courses = repository.findAll(Sort.by(Sort.Direction.DESC, "courseCode"));
        if (!courses.isEmpty()) {
            String lastCourseCode = courses.get(0).getCourseCode();
            String numericPart = lastCourseCode.substring(4);
            int newId = Integer.parseInt(numericPart) + 1;
            String newCourseCode = String.format("MOD-%03d", newId);
            course.setCourseCode(newCourseCode);
        } else {
            course.setCourseCode("MOD-001");
        }
        Course savedCourse = repository.save(course);

        // Add the courseCode to the relevant faculty's courseCode list
        Faculty faculty = facultyService.getFacultyByFacultyId(course.getFacultyId());
        faculty.getCourseCode().add(savedCourse.getCourseCode());
        facultyService.updateFaculty(faculty);

        return savedCourse;
    }

    //read all
    public List<Course> findAllCourses() {
        return repository.findAll();
    }



    public Course getCourseByCourseId(String courseId) {
        // Get the username from the JWT token
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // Get the user's ID using the username
        String userId = userInfoService.getUserByUsername(username).getId();

        // Check if a document exists with the user's ID and the course code
        boolean isEnrolled = enrollmentService.isStudentEnrolledInCourse(userId, courseId);

        if (!isEnrolled) {
            throw new IllegalArgumentException("ERROR : User is not enrolled in this course");
        }

        // If a match is found, proceed with the getCourseByCourseId(courseId) method
        return repository.findById(courseId).get();
    }



    public Course updateCourse(Course courseRequest) {
        // Get the existing course from DB
        Course existingCourse = repository.findById(courseRequest.getCourseCode()).orElse(null);

        // Check if the facultyId is being updated
        if (!existingCourse.getFacultyId().equals(courseRequest.getFacultyId())) {
            // Get the current faculty and remove the course code from its list
            Faculty currentFaculty = facultyService.getFacultyByFacultyId(existingCourse.getFacultyId());
            currentFaculty.getCourseCode().remove(existingCourse.getCourseCode());
            facultyService.updateFaculty(currentFaculty);

            // Get the new faculty and add the course code to its list
            Faculty newFaculty = facultyService.getFacultyByFacultyId(courseRequest.getFacultyId());
            newFaculty.getCourseCode().add(courseRequest.getCourseCode());
            facultyService.updateFaculty(newFaculty);
        }

        // Update the fields of the existing course
        existingCourse.setCourseName(courseRequest.getCourseName());
        existingCourse.setDescription(courseRequest.getDescription());
        existingCourse.setCourseCode(courseRequest.getCourseCode());
        existingCourse.setCredits(courseRequest.getCredits());
        existingCourse.setFacultyId(courseRequest.getFacultyId());

        // Save the updated course back to the database
        return repository.save(existingCourse);
    }

    public String deleteCourse(String courseId) {
        // Get the course to be deleted
        Course course = repository.findById(courseId).orElse(null);
        if (course == null) {
            throw new IllegalArgumentException("Invalid course ID");
        }

        // Get the faculty of the course
        Faculty faculty = facultyService.getFacultyByFacultyId(course.getFacultyId());

        // Remove the courseCode from the faculty's courseCode list
        faculty.getCourseCode().remove(course.getCourseCode());
        facultyService.updateFaculty(faculty);

        // Delete the course from the database
        repository.deleteById(courseId);
        return courseId + " course deleted from dashboard ";
    }





}
