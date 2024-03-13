package org.demo.movieticketbooking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "screens")
@Data
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String totalSeats;
    @ManyToOne
    @JoinColumn(name = "cinemaId")
    private Cinema cinema;

}
