package com.learnium.service;

import com.learnium.model.Enrollment;
import com.learnium.repository.CourseRepository;
import com.learnium.repository.EnrollmentRepository;
import com.learnium.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;



    public Enrollment addEnrollment(Enrollment enrollment) {

        // Check if course code is valid
        if (!courseRepository.existsByCourseCode(enrollment.getCourseCode())) {
            throw new IllegalArgumentException("Invalid course code");
        }

        // Check if studentIds are valid
        for (String studentId : enrollment.getStudentId()) {
            if (!userInfoRepository.existsById(studentId)) {
                throw new IllegalArgumentException("Invalid student id: " + studentId);
            }
        }

        List<Enrollment> faculties = enrollmentRepository.findAll(Sort.by(Sort.Direction.DESC, "enrollmentId"));
        if (!faculties.isEmpty()) {
            String lastEnrollmentId = faculties.get(0).getEnrollmentId();
            String numericPart = lastEnrollmentId.substring(4);
            int newId = Integer.parseInt(numericPart) + 1;
            String newEnrollmentId = String.format("ENR-%03d", newId);
            enrollment.setEnrollmentId(newEnrollmentId);
        } else {
            enrollment.setEnrollmentId("ENR-001");
        }
        return enrollmentRepository.save(enrollment);
    }
    //read all
    public List<Enrollment> findAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    //read one
//    public Enrollment getEnrollmentByEnrollmentId(String enrollmentId) {
//        return enrollmentRepository.findById(enrollmentId).get();
//    }

    public Enrollment getEnrollmentByEnrollmentId(String enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid enrollment id: " + enrollmentId));
    }


//    public Enrollment updateEnrollment(Enrollment enrollmentRequest) {
//        //get the existing document from DB
//        // populate new value from request to existing object/entity/document
//        Enrollment existingEnrollment = enrollmentRepository.findById(enrollmentRequest.getEnrollmentId()).get();
//        existingEnrollment.setCourseCode(enrollmentRequest.getCourseCode());
//        existingEnrollment.setStudentId(enrollmentRequest.getStudentId());
//        return enrollmentRepository.save(existingEnrollment);
//    }


    public Enrollment updateEnrollment(Enrollment enrollmentRequest) {
        Enrollment existingEnrollment = enrollmentRepository.findById(enrollmentRequest.getEnrollmentId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid enrollment id: " + enrollmentRequest.getEnrollmentId()));
        existingEnrollment.setCourseCode(enrollmentRequest.getCourseCode());
        existingEnrollment.setStudentId(enrollmentRequest.getStudentId());
        return enrollmentRepository.save(existingEnrollment);
    }

    public String deleteEnrollment(String enrollmentId) {
        enrollmentRepository.deleteById(enrollmentId);
        return enrollmentId + " enrollment deleted from dashboard ";
    }



    public boolean isStudentEnrolledInCourse(String studentId, String courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseCode(studentId, courseId);
    }
}
