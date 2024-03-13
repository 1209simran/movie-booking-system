package org.demo.movieticketbooking.dto;

import lombok.Data;

@Data
public class ScreenDto {

    private String name;
    private String totalSeats;
    private Long cinemaId;

}
