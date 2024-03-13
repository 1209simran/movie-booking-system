package org.demo.movieticketbooking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cinemas")
@Data
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "locationId")
    private Location location;
}
