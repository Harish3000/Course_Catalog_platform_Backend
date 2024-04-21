package com.learnium.service;

import com.learnium.service.EnrollmentService;
import com.learnium.service.UserInfoService;
import com.learnium.model.TimeTable;
import com.learnium.model.Enrollment;
import com.learnium.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(TimeTable existingTimeTable) {
        String courseCode = existingTimeTable.getCourseCode();

        List<Enrollment> enrolled = enrollmentService.findAllEnrollments();
        List<String> studentIds = enrolled.stream()
                .filter(e -> e.getCourseCode().equals(courseCode))
                .map(Enrollment::getStudentId)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        List<UserInfo> appUsers = userInfoService.getAllUsers();
        List<String> emails = appUsers.stream()
                .filter(u -> studentIds.contains(u.getId()))
                .map(UserInfo::getEmail)
                .collect(Collectors.toList());

        for (String email : emails) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Learnium Time Table Update");
            message.setText("The time table has been updated for course: " + courseCode);
            emailSender.send(message);
        }
    }
}