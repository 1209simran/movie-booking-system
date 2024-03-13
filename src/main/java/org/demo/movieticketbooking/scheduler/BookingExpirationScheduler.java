package org.demo.movieticketbooking.scheduler;

import org.demo.movieticketbooking.model.ShowSeat;
import org.demo.movieticketbooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingExpirationScheduler {
    @Autowired
    private BookingService bookingService;

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(fixedRate = 600000) // scheduler will run at every 10 minutes
    public void releaseExpiredSeats() {
        //find all the expired seats and then make it available
        List<ShowSeat> expiredSeats = bookingService.getExpiredSeats();
        bookingService.releaseExpiredSeats(expiredSeats);
    }
}
