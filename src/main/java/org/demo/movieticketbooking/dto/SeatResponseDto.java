package org.demo.movieticketbooking.dto;

import lombok.Data;
import org.demo.movieticketbooking.model.CinemaSeat;

@Data
public class SeatResponseDto {

    private Long id;
    private CinemaSeat cinemaSeat;
    private String status;
    private Long showId;
}
