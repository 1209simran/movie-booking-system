package org.demo.movieticketbooking.repository;

import org.demo.movieticketbooking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {


    Optional<Booking> findById(Long id);
}
