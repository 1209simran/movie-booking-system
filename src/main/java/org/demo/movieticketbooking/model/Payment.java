package org.demo.movieticketbooking.model;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "payment")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    private String transactionId;
    private String paymentMethod;
    private Timestamp createdTimestamp;
    private String status;

    @ManyToOne
    @JoinColumn(name = "bookingId")
    private Booking booking;
}
