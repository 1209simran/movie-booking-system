package org.demo.movieticketbooking.service;

import org.demo.movieticketbooking.dto.BookingRequestDto;
import org.demo.movieticketbooking.dto.BookingResponseDto;
import org.demo.movieticketbooking.dto.PaymentRequestDto;
import org.demo.movieticketbooking.model.Payment;
import org.demo.movieticketbooking.model.ShowSeat;

import java.util.List;

public interface BookingService {

    Payment confirmSeats(PaymentRequestDto paymentRequestDto);

    BookingResponseDto bookTicket(BookingRequestDto bookingRequestDto);

    List<ShowSeat> getExpiredSeats();

    void releaseExpiredSeats(List<ShowSeat> expiredBookings);

    void updateBookingStatus(Long id, String status);
}
