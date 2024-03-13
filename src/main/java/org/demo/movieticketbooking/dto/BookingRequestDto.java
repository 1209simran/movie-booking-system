package org.demo.movieticketbooking.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequestDto {

    private List<Long> seatIds;
    private Long showId;
    private Long userId;
}
