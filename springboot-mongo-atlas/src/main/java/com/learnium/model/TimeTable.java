package com.learnium.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "TimeTable")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeTable {
    @Id
    private String timeTableId;

    @NotNull(message = "Date is mandatory")
    private LocalDate date;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @NotBlank(message = "Course Code is mandatory")
    private String courseCode;

    @NotBlank(message = "Room ID is mandatory")
    private String roomId;

    private List<String> resourceIds;

    @NotBlank(message = "Faculty ID is mandatory")
    private String facultyId;
}