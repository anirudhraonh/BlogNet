package com.example.BlogNet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostRequestDTO {
    @NotBlank(message = "Title can't be blank.")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Body is required")
    @Size(min = 10, message = "Body must be at least 10 characters")
    private String body;
}
