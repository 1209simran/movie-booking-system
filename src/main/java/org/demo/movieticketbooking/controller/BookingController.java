package org.demo.movieticketbooking.controller;

import lombok.extern.slf4j.Slf4j;
import org.demo.movieticketbooking.constants.ErrorConstant;
import org.demo.movieticketbooking.dto.BookingRequestDto;
import org.demo.movieticketbooking.dto.BookingResponseDto;
import org.demo.movieticketbooking.dto.PaymentRequestDto;
import org.demo.movieticketbooking.enums.Status;
import org.demo.movieticketbooking.model.Payment;
import org.demo.movieticketbooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/bookTicket")
    public ResponseEntity<?> bookTicket(@RequestBody BookingRequestDto bookingRequestDto) {
        try {
            BookingResponseDto bookingResponseDto = bookingService.bookTicket(bookingRequestDto);
            return new ResponseEntity<>(bookingResponseDto, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ErrorConstant.UNABLE_TO_BOOK_TICKET, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/confirmSeats")
    public ResponseEntity<?> confirmSeats(@RequestBody PaymentRequestDto paymentRequestDto) {
        try {
            Payment payment = bookingService.confirmSeats(paymentRequestDto);
            return new ResponseEntity<>(payment, HttpStatus.CREATED);
        } catch (Exception ex) {
            bookingService.updateBookingStatus(paymentRequestDto.getBookingId(), Status.FAILED.toString());
            return new ResponseEntity<>(ErrorConstant.PAYMENT_FAILED, HttpStatus.BAD_REQUEST);
        }
    }
}
