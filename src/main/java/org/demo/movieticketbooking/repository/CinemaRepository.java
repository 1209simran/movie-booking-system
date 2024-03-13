package org.demo.movieticketbooking.repository;

import org.demo.movieticketbooking.model.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {

    Optional<Cinema> findById(Long id);

    List<Cinema> findCinemaByLocationId(Long locationId);

    @Query(value = "select c.* from bookmyshow.shows s2\n" +
            "left join bookmyshow.screens s on s2.screen_id =s.id \n" +
            "left join bookmyshow.cinemas c on c.id =s.cinema_id " +
            "where c.location_id =?1 and s2.movie_id =?2 group by c.id \n", nativeQuery = true)
    List<Cinema> getAllCinemasByMovieIdAndLocationId(Long locationId, Long movieId);
}
