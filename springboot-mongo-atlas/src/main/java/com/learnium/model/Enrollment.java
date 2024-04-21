package com.learnium.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Enrollment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment {
    @Id
    private String enrollmentId;

    @NotBlank(message = "Course Code is mandatory")
    private String courseCode;

    @NotEmpty(message = "Student ID list cannot be empty")
    private List<String> studentId;
}