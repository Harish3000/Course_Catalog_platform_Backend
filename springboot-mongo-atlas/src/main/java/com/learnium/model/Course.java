package com.learnium.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "course")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    private String courseCode;

    @NotBlank(message = "Course name is mandatory")
    @Size(max = 50, message = "Course name must be less than 50 characters")
    private String courseName;

    @NotBlank(message = "Credits is mandatory")
    private String credits;

    @Size(max = 200, message = "Description must be less than 200 characters")
    private String description;

    @NotBlank(message = "Faculty ID is mandatory")
    private String facultyId;
}