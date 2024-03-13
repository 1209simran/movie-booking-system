package org.demo.movieticketbooking.service;

import lombok.extern.slf4j.Slf4j;
import org.demo.movieticketbooking.dto.CinemaDto;
import org.demo.movieticketbooking.dto.CinemaSeatDto;
import org.demo.movieticketbooking.dto.ScreenDto;
import org.demo.movieticketbooking.dto.SeatResponseDto;
import org.demo.movieticketbooking.enums.SeatType;
import org.demo.movieticketbooking.model.*;
import org.demo.movieticketbooking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CinemaServiceImpl implements CinemaService {

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private CinemaSeatRepository cinemaSeatRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    private Cinema setCinemaDetails(CinemaDto cinemaDto) {
        try {
            Optional<Location> location = locationRepository.findById(cinemaDto.getLocationId());
            List<Location> locations = location.stream().toList();
            if (locations.size() == 0) {
                log.info("No location found, please try different location id");
                return null;
            }
            Cinema cinema = new Cinema();
            cinema.setName(cinemaDto.getName());
            cinema.setLocation(locations.get(0));
            return cinema;
        } catch (Throwable tx) {
            log.error("Error in saving cinema: {}", cinemaDto);
        }
        return null;
    }

    public Cinema addCinema(CinemaDto cinemaDto) {
        try {
            Cinema cinema = setCinemaDetails(cinemaDto);
            if (cinema == null) {
                log.error("Error in saving cinema: {}", cinemaDto);
                return null;
            }
            cinemaRepository.save(cinema);
            return cinema;
        } catch (Throwable ex) {
            log.error("Error in saving cinema: {}", cinemaDto);
        }
        return null;
    }

    @Override
    public Cinema updateCinema(Cinema cinema) {
        try {
            Optional<Cinema> cinemaNew = cinemaRepository.findById(cinema.getId());
            List<Cinema> cinemas = cinemaNew.stream().toList();
            if (cinemas.size() == 0) {
                log.info("No cinema found for id: {}", cinema.getId());
                return null;
            }
            for (Cinema cin : cinemas) {
                cin.setName(cinema.getName());
                cin.getLocation().setId(cinema.getLocation().getId());
                cinemaRepository.save(cin);
            }
            return cinema;
        } catch (Throwable ex) {
            log.error("Error in updating cinema id: {}", cinema.getId());
        }
        return null;
    }

    @Override
    public List<Cinema> getAllCinemas() {
        List<Cinema> cinemas = new ArrayList<>();
        cinemas.addAll(cinemaRepository.findAll());
        return cinemas;
    }

    @Override
    public List<Cinema> getCinemaByLocation(Long locationId) {
        try {
            return cinemaRepository.findCinemaByLocationId(locationId);
        } catch (Throwable ex) {
            log.error("Error in retrieving cinemas for locationId - : {}", locationId);
        }
        return null;
    }

    @Override
    public Screen addScreens(ScreenDto screenDto) {
        try {
            Optional<Cinema> cinema = cinemaRepository.findById(screenDto.getCinemaId());
            List<Cinema> cinemas = cinema.stream().toList();
            if (cinemas.size() == 0) {
                log.info("No cinema found for id: {}", screenDto.getCinemaId());
                return null;
            }
            Screen screen = new Screen();
            screen.setCinema(cinemas.get(0));
            screen.setName(screenDto.getName());
            screen.setTotalSeats(screenDto.getTotalSeats());
            screenRepository.save(screen);
            return screen;
        } catch (Throwable ex) {
            log.error("Error in adding screen");
        }
        return null;
    }

    @Override
    public List<Screen> getScreensByCinemaId(Long screenId) {
        try {
            return screenRepository.findScreenByCinemaId(screenId);
        } catch (Throwable ex) {
            log.error("Error in retrieving screens for screenId - : {}", screenId);
        }
        return null;
    }

    @Override
    public CinemaSeat addCinemaSeat(CinemaSeatDto cinemaSeatDto) {
        try {
            Optional<Screen> screen = screenRepository.findById(cinemaSeatDto.getScreenId());
            List<Screen> screens = screen.stream().toList();
            if (screens.size() == 0) {
                log.info("No screen found for id: {}", cinemaSeatDto.getScreenId());
                return null;
            }
            CinemaSeat cinemaSeat = new CinemaSeat();
            cinemaSeat.setSeatNumber(cinemaSeatDto.getSeatNumber());
            cinemaSeat.setScreen(screens.get(0));
            cinemaSeat.setType(SeatType.valueOf(cinemaSeatDto.getType()).toString());
            cinemaSeat.setPrice(cinemaSeatDto.getPrice());
            cinemaSeatRepository.save(cinemaSeat);
            return cinemaSeat;
        } catch (Throwable ex) {
            log.error("Error in adding seat");
        }
        return null;
    }

    @Override
    public List<CinemaSeat> getSeatByScreenId(Long screenId) {
        try {
            return cinemaSeatRepository.findSeatByScreenId(screenId);
        } catch (Throwable ex) {
            log.error("Error in retrieving seats for screenId - : {}", screenId);
        }
        return null;
    }

    @Override
    public List<Cinema> getAllCinemasByMovieIdAndLocationId(Long locationId, Long movieId) {
        try {
            return cinemaRepository.getAllCinemasByMovieIdAndLocationId(locationId, movieId);
        } catch (Throwable ex) {
            log.error("Error in fetching cinemas");
        }
        return null;
    }

    @Override
    public List<SeatResponseDto> getSeatsByShowId(Long showId) {
        try {
            List<ShowSeat> seats = showSeatRepository.findShowSeatsByShowsId(showId);
            return setSeatDetails(seats, showId);
        } catch (Throwable ex) {
            log.error("Error in retrieving seats for showId - : {}", showId);
        }
        return null;
    }

    private List<SeatResponseDto> setSeatDetails(List<ShowSeat> seats, Long showId) {
        List<SeatResponseDto> seatResponseDtoList = new ArrayList<>();
        seats.forEach(seat -> {
                    SeatResponseDto seatResponseDto = new SeatResponseDto();
                    seatResponseDto.setId(seat.getId());
                    seatResponseDto.setCinemaSeat(seat.getCinemaSeat());
                    seatResponseDto.setStatus(seat.getStatus());
                    seatResponseDto.setShowId(showId);
                    seatResponseDtoList.add(seatResponseDto);

                }
        );
        return seatResponseDtoList;
    }
}
