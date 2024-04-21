package com.learnium.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "faculty")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Faculty {
    @Id
    private String facultyId;

    @NotBlank(message = "Faculty name is mandatory")
    private String facultyName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Designation is mandatory")
    private String designation;

    @NotBlank(message = "Department is mandatory")
    private String department;

    private List<String> courseCode;
}