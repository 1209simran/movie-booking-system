package org.demo.movieticketbooking.repository;

import org.demo.movieticketbooking.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findById(Long id);

    @Query(value = "select m.* from bookmyshow.shows s2 \n" +
            "left join bookmyshow.movies m  on m.id =s2.movie_id \n" +
            "left join bookmyshow.screens s on s2.screen_id =s.id \n" +
            "left join bookmyshow.cinemas c on c.id =s.cinema_id  \n" +
            "where c.location_id =?1 group by m.id ", nativeQuery = true)
    List<Movie> getAllMoviesByLocationId(Long id);
}
