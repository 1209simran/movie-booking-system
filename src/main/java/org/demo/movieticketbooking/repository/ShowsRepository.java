package org.demo.movieticketbooking.repository;

import org.demo.movieticketbooking.model.Shows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowsRepository extends JpaRepository<Shows, Long> {

    Optional<Shows> findById(Long id);

    List<Shows> findShowsByMovieId(Long movieId);

    List<Shows> getShowsByMovieIdAndScreenId(Long movieId, Long screenId);

    @Query(value = "select s.* from bookmyshow.shows s\n" +
            "left join bookmyshow.screens s2 on s2.id =s.screen_id \n" +
            "where s2.cinema_id =?1 and s.movie_id =?2", nativeQuery = true)
    List<Shows> getAllShowsByCinemaIdAndMovieId(Long cinemaId, Long movieId);
}
