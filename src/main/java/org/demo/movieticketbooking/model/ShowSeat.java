package org.demo.movieticketbooking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "show_seat")
@Data
public class ShowSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private double price;

    @ManyToOne
    @JoinColumn(name = "showId")
    private Shows shows;

    @ManyToOne
    @JoinColumn(name = "bookingId")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "seatId")
    private CinemaSeat cinemaSeat;


}
