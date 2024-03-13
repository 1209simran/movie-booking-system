package org.demo.movieticketbooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowsDto {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long screenId;
    private Long movieId;

}
