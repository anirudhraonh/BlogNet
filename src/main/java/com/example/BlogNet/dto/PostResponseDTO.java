package com.example.BlogNet.dto;

import com.example.BlogNet.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponseDTO {
    private Long id;
    private String title;
    private String body;
    private LocalDateTime createdAt;
    private String authorName;
    private Long authorId;
}
