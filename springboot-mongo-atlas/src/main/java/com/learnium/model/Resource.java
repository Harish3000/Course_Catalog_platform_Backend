package com.learnium.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "Resource")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resource {
    @Id
    private String resourceId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Type is mandatory")
    private String type;

    private LocalDate reservedDate;
}