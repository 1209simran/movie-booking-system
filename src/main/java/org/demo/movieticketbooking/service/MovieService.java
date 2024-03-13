package org.demo.movieticketbooking.service;

import org.demo.movieticketbooking.dto.MovieDto;
import org.demo.movieticketbooking.dto.MovieResponseDto;
import org.demo.movieticketbooking.dto.ShowsDto;
import org.demo.movieticketbooking.model.Movie;
import org.demo.movieticketbooking.model.Shows;

import java.util.List;

public interface MovieService {

    Movie addMovie(MovieDto movieDto);

    MovieResponseDto updateMovie(MovieResponseDto movie);

    List<MovieResponseDto> getAllMovies();

    Shows addShows(ShowsDto showsDto);

    List<Shows> getShowsByMovieId(Long movieId);

    List<Shows> getShowsByMovieIdAndScreenId(Long movieId, Long screenId);

    List<MovieResponseDto> getAllMoviesByLocationId(Long locationId);

    List<Shows> getAllShowsByCinemaIdAndMovieId(Long cinamaId, Long movieId);
}
