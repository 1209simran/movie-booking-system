package org.demo.movieticketbooking.repository;

import org.demo.movieticketbooking.model.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {

    Optional<Screen> findById(Long id);

    List<Screen> findScreenByCinemaId(Long cinemaId);
}
