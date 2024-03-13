package org.demo.movieticketbooking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cinema_seat")
@Data
public class CinemaSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String seatNumber;
    private double price;

    @ManyToOne
    @JoinColumn(name = "screenId")
    private Screen screen;
}
