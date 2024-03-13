package org.demo.movieticketbooking.service;

import lombok.extern.slf4j.Slf4j;
import org.demo.movieticketbooking.dto.MovieDto;
import org.demo.movieticketbooking.dto.MovieResponseDto;
import org.demo.movieticketbooking.dto.ShowsDto;
import org.demo.movieticketbooking.enums.Status;
import org.demo.movieticketbooking.model.*;
import org.demo.movieticketbooking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowsRepository showsRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Autowired
    private CinemaSeatRepository cinemaSeatRepository;

    public Movie addMovie(MovieDto movieDto) {
        try {
            Movie movie = new Movie();
            movie.setTitle(movieDto.getTitle());
            movie.setDuration(movieDto.getDuration());

            movie.setGenre(String.join(",", movieDto.getGenre()));
            movie.setLanguage(String.join(",", movieDto.getLanguage()));
            movie.setDescription(movieDto.getDescription());
            movie.setReleaseDate(movieDto.getReleaseDate());
            movieRepository.save(movie);
            return movie;
        } catch (Throwable ex) {
            log.error("Error in saving location: {}", movieDto);
        }
        return null;
    }

    @Override
    public MovieResponseDto updateMovie(MovieResponseDto movie) {
        try {
            Optional<Movie> locationNew = movieRepository.findById(movie.getId());
            List<Movie> movies = locationNew.stream().toList();
            if (movies.size() == 0) {
                log.info("No movie found for id: {}", movie.getId());
                return null;
            }
            for (Movie mov : movies) {
                mov.setTitle(movie.getTitle());
                mov.setDuration(movie.getDuration());
                mov.setGenre(String.join(",", movie.getGenre()));
                mov.setLanguage(String.join(",", movie.getLanguage()));
                mov.setDescription(movie.getDescription());
                mov.setReleaseDate(movie.getReleaseDate());
                movieRepository.save(mov);
            }
            return movie;
        } catch (Throwable ex) {
            log.error("Error in updating movie id: {}", movie.getId());
        }
        return null;
    }

    @Override
    public List<MovieResponseDto> getAllMovies() {
        List<MovieResponseDto> movies = new ArrayList<>();
        List<Movie> movie = movieRepository.findAll();
        movie.forEach(m -> {
            MovieResponseDto movieResponseDto = new MovieResponseDto();
            movieResponseDto.setDescription(m.getDescription());
            movieResponseDto.setId(m.getId());
            movieResponseDto.setDuration(m.getDuration());
            movieResponseDto.setGenre(m.getGenre().split(","));
            movieResponseDto.setTitle(m.getTitle());
            movieResponseDto.setReleaseDate(m.getReleaseDate());
            movieResponseDto.setLanguage(m.getLanguage().split(","));
            movies.add(movieResponseDto);
        });

        return movies;
    }

    @Override
    @Transactional
    public Shows addShows(ShowsDto showsDto) {
        try {
            Optional<Movie> movie = movieRepository.findById(showsDto.getMovieId());
            List<Movie> movies = movie.stream().toList();
            Optional<Screen> screen = screenRepository.findById(showsDto.getScreenId());
            List<Screen> screens = screen.stream().toList();
            if (screens.isEmpty() || movies.isEmpty()) {
                log.info("No movie/screen found for movieId: {} and screenId: {}", showsDto.getMovieId(), showsDto.getScreenId());
                return null;
            }
            Shows shows = new Shows();
            shows.setMovie(movies.get(0));
            shows.setScreen(screens.get(0));
            shows.setStartTime(showsDto.getStartTime());
            shows.setEndTime(showsDto.getEndTime());
            showsRepository.save(shows);
            addSeatsForMovieShow(shows);
            return shows;
        } catch (Throwable ex) {
            log.error("Error in adding a movie show");
        }
        return null;
    }

    @Transactional
    protected void addSeatsForMovieShow(Shows shows) {
        try {
            List<CinemaSeat> cinemaSeats = cinemaSeatRepository.findSeatByScreenId(shows.getScreen().getId());
            cinemaSeats.forEach(cinemaSeat -> {
                ShowSeat showSeat = new ShowSeat();
                showSeat.setShows(shows);
                showSeat.setCinemaSeat(cinemaSeat);
                showSeat.setBooking(null);
                showSeat.setPrice(cinemaSeat.getPrice());
                showSeat.setStatus(Status.AVAILABLE.toString());
                showSeatRepository.save(showSeat);
            });
        } catch (Throwable ex) {
            log.error("Error in saving seats for movie showId - : {}", shows.getId());
        }
    }

    @Override
    public List<Shows> getShowsByMovieId(Long movieId) {
        try {
            return showsRepository.findShowsByMovieId(movieId);
        } catch (Throwable ex) {
            log.error("Error in retrieving movie shows for movieId - : {}", movieId);
        }
        return null;
    }

    public List<Shows> getShowsByMovieIdAndScreenId(Long movieId, Long screenId) {
        try {
            return showsRepository.getShowsByMovieIdAndScreenId(movieId, screenId);
        } catch (Throwable ex) {
            log.error("Error in retrieving movie shows for movieId - : {} and screenId: {}", movieId, screenId);
        }
        return null;
    }

    @Override
    public List<MovieResponseDto> getAllMoviesByLocationId(Long locationId) {
        try {
            List<Movie> movies = movieRepository.getAllMoviesByLocationId(locationId);
            if (movies.isEmpty()) {
                log.info("No movies for location id: {}", locationId);
                return null;
            }
            List<MovieResponseDto> movieResponseList = new ArrayList<>();
            movies.forEach(m -> {
                MovieResponseDto movieResponseDto = new MovieResponseDto();
                movieResponseDto.setDescription(m.getDescription());
                movieResponseDto.setId(m.getId());
                movieResponseDto.setDuration(m.getDuration());
                movieResponseDto.setGenre(m.getGenre().split(","));
                movieResponseDto.setTitle(m.getTitle());
                movieResponseDto.setReleaseDate(m.getReleaseDate());
                movieResponseDto.setLanguage(m.getLanguage().split(","));
                movieResponseList.add(movieResponseDto);
            });
            return movieResponseList;
        } catch (Throwable ex) {
            log.error("Error in adding a movie show");
        }
        return null;
    }

    @Override
    public List<Shows> getAllShowsByCinemaIdAndMovieId(Long cinemaId, Long movieId) {
        try {
            return showsRepository.getAllShowsByCinemaIdAndMovieId(cinemaId, movieId);
        } catch (Throwable ex) {
            log.error("Error in retrieving shows for movieId - : {} and cinemaId: {}", movieId, cinemaId);
        }
        return null;
    }


}
