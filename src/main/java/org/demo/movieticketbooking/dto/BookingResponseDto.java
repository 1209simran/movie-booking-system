package org.demo.movieticketbooking.dto;

import lombok.Data;
import org.demo.movieticketbooking.model.Booking;

import java.util.List;

@Data
public class BookingResponseDto {

    private Booking booking;
    private List<Long> seatIds;
}
