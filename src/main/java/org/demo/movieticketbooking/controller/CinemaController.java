package org.demo.movieticketbooking.controller;

import lombok.extern.slf4j.Slf4j;
import org.demo.movieticketbooking.dto.CinemaDto;
import org.demo.movieticketbooking.dto.CinemaSeatDto;
import org.demo.movieticketbooking.dto.ScreenDto;
import org.demo.movieticketbooking.dto.SeatResponseDto;
import org.demo.movieticketbooking.model.Cinema;
import org.demo.movieticketbooking.model.CinemaSeat;
import org.demo.movieticketbooking.model.Screen;
import org.demo.movieticketbooking.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
//@RequestMapping("/api")
public class CinemaController {

    @Autowired
    private CinemaService cinemaService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/api/cinema/addCinema")
    public ResponseEntity<?> addCinema(@RequestBody CinemaDto cinemaDto) {
        Cinema cinema = cinemaService.addCinema(cinemaDto);
        if (cinema == null) {
            return new ResponseEntity<>("Unable to add cinema, please try again!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cinema, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/api/cinema/updateCinema")
    public ResponseEntity<?> updateCinema(@RequestBody Cinema mov) {
        Cinema cinema = cinemaService.updateCinema(mov);
        if (cinema == null) {
            return new ResponseEntity<>("Unable to update cinema, please try again!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cinema, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/api/cinema/getAllCinemas")
    public ResponseEntity<List<Cinema>> getAllCinema() {
        List<Cinema> cinemas = cinemaService.getAllCinemas();
        return new ResponseEntity<>(cinemas, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/api/cinema/addScreens")
    public ResponseEntity<?> addScreens(@RequestBody ScreenDto screenDto) {
        Screen screen = cinemaService.addScreens(screenDto);
        if (screen == null) {
            return new ResponseEntity<>("Unable to add screen, please try again!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(screen, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/api/cinema/addCinemaSeat")
    public ResponseEntity<?> addCinemaSeat(@RequestBody CinemaSeatDto cinemaSeatDto) {
        CinemaSeat cinemaSeat = cinemaService.addCinemaSeat(cinemaSeatDto);
        if (cinemaSeat == null) {
            return new ResponseEntity<>("Unable to add cinema seat, please try again!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cinemaSeat, HttpStatus.CREATED);
    }

//    @GetMapping("/getSeatByScreenId")
//    public ResponseEntity<List<CinemaSeat>> getSeatByScreenId(@RequestParam Long screenId) {
//        List<CinemaSeat> cinemaSeats = cinemaService.getSeatByScreenId(screenId);
//        return new ResponseEntity<>(cinemaSeats, HttpStatus.CREATED);
//    }

//    @GetMapping("/getCinemaByLocationId")
//    public ResponseEntity<List<Cinema>> getCinemaByLocationId(@RequestParam Long locationId) {
//        List<Cinema> cinemas = cinemaService.getCinemaByLocation(locationId);
//        return new ResponseEntity<>(cinemas, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/getScreensByCinemaId")
//    public ResponseEntity<List<Screen>> getScreensByCinemaId(@RequestParam Long cinemaId) {
//        List<Screen> screens = cinemaService.getScreensByCinemaId(cinemaId);
//        return new ResponseEntity<>(screens, HttpStatus.CREATED);
//    }

    // Step 2
    @GetMapping("/apiV1/getAllCinemasByMovieIdAndLocationId")
    public ResponseEntity<List<Cinema>> getAllCinemasByMovieIdAndLocationId(@RequestParam Long locationId,
                                                                            @RequestParam Long movieId) {
        List<Cinema> cinemas = cinemaService.getAllCinemasByMovieIdAndLocationId(locationId, movieId);
        return new ResponseEntity<>(cinemas, HttpStatus.CREATED);
    }

    // Step 4
    @GetMapping("/apiV1/getSeatsByShowId")
    public ResponseEntity<List<SeatResponseDto>> getSeatsByShowId(@RequestParam Long showId) {
        List<SeatResponseDto> seats = cinemaService.getSeatsByShowId(showId);
        return new ResponseEntity<>(seats, HttpStatus.CREATED);
    }
}
