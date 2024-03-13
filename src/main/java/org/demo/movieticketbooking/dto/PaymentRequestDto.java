package org.demo.movieticketbooking.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaymentRequestDto {

    private Long bookingId;
    private Long userId;
    private String paymentMethod;
    private List<Long> seatids;
}
