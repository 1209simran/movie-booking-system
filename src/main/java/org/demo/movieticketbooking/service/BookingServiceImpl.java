package org.demo.movieticketbooking.service;

import lombok.extern.slf4j.Slf4j;
import org.demo.movieticketbooking.constants.ErrorConstant;
import org.demo.movieticketbooking.dto.BookingRequestDto;
import org.demo.movieticketbooking.dto.BookingResponseDto;
import org.demo.movieticketbooking.dto.PaymentRequestDto;
import org.demo.movieticketbooking.enums.PaymentMethod;
import org.demo.movieticketbooking.enums.Status;
import org.demo.movieticketbooking.model.*;
import org.demo.movieticketbooking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowsRepository showsRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public BookingResponseDto bookTicket(BookingRequestDto bookingRequestDto) {
        try {
            List<ShowSeat> availableSeats =
                    showSeatRepository.findShowSeatsByCinemaSeatIdInAndShowsId(bookingRequestDto.getSeatIds(),
                            bookingRequestDto.getShowId());
            // added thread.sleep method to check for concurrency issue
            // Thread.sleep(60000);
            if (!validateSeatsAvailability(availableSeats)) {
                throw new RuntimeException(ErrorConstant.SEATS_UNAVAILABLE);
            }
            if (availableSeats.size() == bookingRequestDto.getSeatIds().size()) {
                double amount = calculatePrice(availableSeats);
                Booking booking = saveBookingAndUpdate(bookingRequestDto, amount);
                return setBookingResponse(bookingRequestDto, booking);
            } else {
                throw new RuntimeException(ErrorConstant.SEATS_UNAVAILABLE);
            }
        } catch (Throwable tx) {
            throw new RuntimeException(ErrorConstant.SEATS_UNAVAILABLE);
        }
    }

    @Transactional
    protected Booking saveBookingAndUpdate(BookingRequestDto bookingRequestDto, double amount) {
        try {
            Booking booking = saveBooking(bookingRequestDto, Status.PAYMENT_PENDING.toString(), amount);
            updateShowSeatStatus(bookingRequestDto, booking, Status.PAYMENT_PENDING.toString());
            return booking;
        } catch (Throwable tx) {
            throw new RuntimeException(ErrorConstant.PAYMENT_FAILED);
        }
    }

    @Transactional
    protected void updateShowSeatStatus(BookingRequestDto bookingRequestDto, Booking booking, String status) {
        try {
            HashMap<Long, ShowSeat> showSeatsMap = new HashMap<>();
            List<ShowSeat> showSeatList = showSeatRepository.findShowSeatsByShowsId(bookingRequestDto.getShowId());
            showSeatList.forEach(showSeat -> {
                showSeatsMap.put(showSeat.getCinemaSeat().getId(), showSeat);
            });

            for (int i = 0; i < bookingRequestDto.getSeatIds().size(); i++) {
                Long seat = bookingRequestDto.getSeatIds().get(i);
                ShowSeat showSeat = showSeatsMap.get(seat);
                showSeat.setStatus(status);
                showSeat.setBooking(booking);
                showSeatRepository.save(showSeat);
            }
        } catch (Throwable tx) {
            throw new RuntimeException(ErrorConstant.SEAT_STATUS_ERROR);
        }
    }

    @Transactional
    protected Booking saveBooking(BookingRequestDto bookingRequestDto, String status, double amount) {
        try {
            Booking booking = new Booking();
            Optional<User> users = userRepository.findById(bookingRequestDto.getUserId());
            List<User> user = users.stream().toList();
            Optional<Shows> shows = showsRepository.findById(bookingRequestDto.getShowId());
            List<Shows> showList = shows.stream().toList();
            booking.setStatus(status);
            booking.setUser(user.get(0));
            booking.setShows(showList.get(0));
            booking.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
            booking.setNumberOfSeats((long) bookingRequestDto.getSeatIds().size());
            booking.setAmount(amount);
            bookingRepository.save(booking);
            return booking;
        } catch (Throwable tx) {
            throw new RuntimeException(ErrorConstant.SAVE_BOOKING_ERROR);
        }
    }

    @Transactional
    protected BookingResponseDto setBookingResponse(BookingRequestDto bookingRequestDto, Booking booking) {
        try {
            BookingResponseDto bookingResponseDto = new BookingResponseDto();
            bookingResponseDto.setBooking(booking);
            bookingResponseDto.setSeatIds(bookingRequestDto.getSeatIds());
            return bookingResponseDto;
        } catch (Throwable ex) {
            throw new RuntimeException(ErrorConstant.SAVE_BOOKING_ERROR);
        }
    }

    @Override
    @Transactional
    public Payment confirmSeats(PaymentRequestDto paymentRequestDto) {
        Optional<Booking> bookings = bookingRepository.findById(paymentRequestDto.getBookingId());
        Booking booking = bookings.stream().toList().get(0);
        try {

            boolean isvalid = validateSeat(booking, paymentRequestDto.getSeatids());
            if (!isvalid) {
                throw new RuntimeException(ErrorConstant.SEATS_UNAVAILABLE);
            }
            // do payment
            String transactionId = pay(booking.getAmount());

            // if payment is successful then update transaction table update show seat status with bookingIds
            Payment payment = savePaymentTransaction(paymentRequestDto, transactionId, booking,
                    Status.COMPLETED.toString());
            updateBookingStatus(booking, Status.BOOKED.toString());
            updateShowSeatStatus(booking, Status.BOOKED.toString(), paymentRequestDto.getSeatids());
            return payment;
        } catch (Exception ex) {
            // if payment is failed or timeout in payment gateway, then paymentFailedException is caught in this
            // block and save failed transaction
            throw new RuntimeException(ErrorConstant.PAYMENT_FAILED);
        }
    }

    @Transactional
    protected boolean validateSeat(Booking booking, List<Long> seatIds) {
        try {
            List<ShowSeat> showSeats = showSeatRepository.findByBookingId(booking.getId());
            showSeats.removeIf(showSeat -> !Status.PAYMENT_PENDING.toString().equalsIgnoreCase(showSeat.getStatus()));
            AtomicBoolean isSeatsAvailable = new AtomicBoolean(true);
            if (showSeats.size() != seatIds.size()) return false;
            for (Long id : seatIds) {
                if (!showSeats.stream().anyMatch(seat -> seat.getCinemaSeat().getId() == id)) return false;
            }
            return isSeatsAvailable.get();
        } catch (Exception ex) {
            throw new RuntimeException(ErrorConstant.BOOKING_STATUS_ERROR);
        }
    }

    @Transactional
    protected void updateBookingStatus(Booking booking, String status) {
        try {
            booking.setStatus(status);
            bookingRepository.save(booking);
        } catch (Exception ex) {
            throw new RuntimeException(ErrorConstant.BOOKING_STATUS_ERROR);
        }
    }

    @Transactional
    protected void updateShowSeatStatus(Booking booking, String status, List<Long> seatIds) {
        try {
            BookingRequestDto bookingRequestDto = new BookingRequestDto();
            bookingRequestDto.setSeatIds(seatIds);
            bookingRequestDto.setShowId(booking.getShows().getId());
            bookingRequestDto.setUserId(booking.getUser().getId());
            updateShowSeatStatus(bookingRequestDto, booking, status);
        } catch (Exception ex) {
            throw new RuntimeException(ErrorConstant.BOOKING_STATUS_ERROR);
        }
    }

    @Transactional
    protected String pay(double amount) {
        String transactionId = "123456789";
        //do payment;
        //if failed then throw payment failed exception
        //return transactionId;
        return transactionId;
    }

    @Transactional
    protected Payment savePaymentTransaction(PaymentRequestDto paymentRequestDto, String transactionId,
                                             Booking booking, String status) {
        try {
            Payment payment = new Payment();
            payment.setTransactionId(transactionId);
            payment.setBooking(booking);
            payment.setPaymentMethod(PaymentMethod.valueOf(paymentRequestDto.getPaymentMethod()).toString());
            payment.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
            payment.setAmount(booking.getAmount());
            payment.setStatus(status);
            paymentRepository.save(payment);
            return payment;
        } catch (Exception ex) {
            throw new RuntimeException(ErrorConstant.SAVE_PAYMENT_ERROR);
        }
    }

    @Transactional
    protected double calculatePrice(List<ShowSeat> seats) {
        double amount = 0.0;
        for (ShowSeat seat : seats) {
            amount += seat.getPrice();
        }
        return amount;
    }

    private boolean validateSeatsAvailability(List<ShowSeat> availableSeats) {
        AtomicBoolean isAvailable = new AtomicBoolean(true);
        availableSeats.forEach(seat -> {
            if (!seat.getStatus().equalsIgnoreCase(Status.AVAILABLE.toString())) isAvailable.set(false);
        });
        return isAvailable.get();
    }

    @Override
    public List<ShowSeat> getExpiredSeats() {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        return showSeatRepository.findExpiredSeats(tenMinutesAgo);
    }

    @Override
    public void releaseExpiredSeats(List<ShowSeat> expiredSeats) {
        log.info("Total expired seats {}", expiredSeats.size());
        for (ShowSeat showSeat : expiredSeats) {
            // Release the seats (update status to available)
            showSeat.setStatus(Status.AVAILABLE.toString());
            showSeat.setBooking(null);
            showSeatRepository.save(showSeat);
            log.info("Updating seat id: {} to available", showSeat.getId());
        }
    }

    @Override
    @Transactional
    public void updateBookingStatus(Long id, String status) {
        try {
            Optional<Booking> bookings = bookingRepository.findById(id);
            Booking booking = bookings.stream().toList().get(0);
            updateBookingStatus(booking, status);
        } catch (Throwable tx) {
            throw new RuntimeException(ErrorConstant.BOOKING_STATUS_ERROR);
        }
    }

}
