package org.demo.movieticketbooking.controller;

import lombok.extern.slf4j.Slf4j;
import org.demo.movieticketbooking.dto.MovieDto;
import org.demo.movieticketbooking.dto.MovieResponseDto;
import org.demo.movieticketbooking.dto.ShowsDto;
import org.demo.movieticketbooking.model.Movie;
import org.demo.movieticketbooking.model.Shows;
import org.demo.movieticketbooking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/api/movie/addMovie")
    public ResponseEntity<?> addMovie(@RequestBody MovieDto movieDto) {
        Movie movie = movieService.addMovie(movieDto);
        if (movie == null) {
            return new ResponseEntity<>("Unable to add movie, please try again!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(movie, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/api/movie/updateMovie")
    public ResponseEntity<?> updateMovie(@RequestBody MovieResponseDto mov) {
        MovieResponseDto movie = movieService.updateMovie(mov);
        if (movie == null) {
            return new ResponseEntity<>("Unable to update movie, please try again!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(movie, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/api/movie/getAllMovies")
    public ResponseEntity<List<MovieResponseDto>> getAllMovies() {
        List<MovieResponseDto> movies = movieService.getAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/api/movie/addShows")
    public ResponseEntity<?> addShows(@RequestBody ShowsDto showsDto) {
        Shows shows = movieService.addShows(showsDto);
        if (shows == null) {
            return new ResponseEntity<>("Unable to add movie show, please try again!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(shows, HttpStatus.CREATED);
    }
//
//    @GetMapping("/getShowsByMovieId")
//    public ResponseEntity<List<Shows>> getShowsByMovieId(@RequestParam Long movieId) {
//        List<Shows> shows = movieService.getShowsByMovieId(movieId);
//        return new ResponseEntity<>(shows, HttpStatus.CREATED);
//    }

//    @GetMapping("/getShowsByMovieIdAndScreenId")
//    public ResponseEntity<List<Shows>> getShowsByMovieIdAndScreenId(@RequestParam Long movieId, Long screenId) {
//        List<Shows> shows = movieService.getShowsByMovieIdAndScreenId(movieId, screenId);
//        return new ResponseEntity<>(shows, HttpStatus.CREATED);
//    }

    // Step 1
    @GetMapping("/apiV1/getAllMoviesByLocationId")
    public ResponseEntity<List<MovieResponseDto>> getAllMoviesByLocationId(@RequestParam Long locationId) {
        List<MovieResponseDto> movies = movieService.getAllMoviesByLocationId(locationId);
        return new ResponseEntity<>(movies, HttpStatus.CREATED);
    }

    // Step 3
    @GetMapping("/apiV1/getAllShowsByCinemaIdAndMovieId")
    public ResponseEntity<List<Shows>> getAllShowsByCinemaIdAndMovieId(@RequestParam Long cinamaId,
                                                                       @RequestParam Long movieId) {
        List<Shows> shows = movieService.getAllShowsByCinemaIdAndMovieId(cinamaId, movieId);
        return new ResponseEntity<>(shows, HttpStatus.CREATED);
    }


}
