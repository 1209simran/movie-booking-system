package org.demo.movieticketbooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MovieResponseDto {

    private Long id;
    private String title;
    private String description;
    private String[] genre;
    private String[] language;
    private Long duration;
    private LocalDateTime releaseDate;
}
