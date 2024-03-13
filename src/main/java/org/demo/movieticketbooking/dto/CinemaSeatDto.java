package org.demo.movieticketbooking.dto;

import lombok.Data;

@Data
public class CinemaSeatDto {

    private String type;
    private String seatNumber;
    private Long screenId;
    private double price;
}
