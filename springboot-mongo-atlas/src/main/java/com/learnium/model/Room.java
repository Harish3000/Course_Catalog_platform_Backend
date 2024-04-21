package com.learnium.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "room")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    private String roomId;

    @NotBlank(message = "Floor is mandatory")
    private String floor;

    @NotBlank(message = "Building is mandatory")
    private String building;

    private LocalDate reservedDate;
}