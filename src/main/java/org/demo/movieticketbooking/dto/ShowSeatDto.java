package org.demo.movieticketbooking.dto;

import lombok.Data;
import org.demo.movieticketbooking.model.Booking;
import org.demo.movieticketbooking.model.CinemaSeat;
import org.demo.movieticketbooking.model.Shows;

@Data
public class ShowSeatDto {
    private Long id;
    private String status;
    private String price;

    private Shows shows;

    private Booking booking;

    private CinemaSeat cinemaSeat;


}
