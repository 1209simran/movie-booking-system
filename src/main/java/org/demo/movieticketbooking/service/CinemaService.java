package org.demo.movieticketbooking.service;

import org.demo.movieticketbooking.dto.CinemaDto;
import org.demo.movieticketbooking.dto.CinemaSeatDto;
import org.demo.movieticketbooking.dto.ScreenDto;
import org.demo.movieticketbooking.dto.SeatResponseDto;
import org.demo.movieticketbooking.model.Cinema;
import org.demo.movieticketbooking.model.CinemaSeat;
import org.demo.movieticketbooking.model.Screen;

import java.util.List;

public interface CinemaService {

    Cinema addCinema(CinemaDto cinemaDto);

    Cinema updateCinema(Cinema cinema);

    List<Cinema> getAllCinemas();

    List<Cinema> getCinemaByLocation(Long locationId);

    Screen addScreens(ScreenDto screenDto);

    List<Screen> getScreensByCinemaId(Long screenId);

    CinemaSeat addCinemaSeat(CinemaSeatDto cinemaSeatDto);

    List<CinemaSeat> getSeatByScreenId(Long screenId);

    List<Cinema> getAllCinemasByMovieIdAndLocationId(Long locationId, Long movieId);

    List<SeatResponseDto> getSeatsByShowId(Long showId);
}
