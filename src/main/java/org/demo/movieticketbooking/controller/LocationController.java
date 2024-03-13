package org.demo.movieticketbooking.controller;

import lombok.extern.slf4j.Slf4j;
import org.demo.movieticketbooking.dto.LocationDto;
import org.demo.movieticketbooking.model.Location;
import org.demo.movieticketbooking.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addLocation")
    public ResponseEntity<?> addLocation(@RequestBody LocationDto locationDto) {
        Location location = locationService.addLocation(locationDto);
        if (location == null) {
            return new ResponseEntity<>("Unable to add location, please try again!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateLocation")
    public ResponseEntity<?> updateLocation(@RequestBody Location loc) {
        Location location = locationService.updateLocation(loc);
        if (location == null) {
            return new ResponseEntity<>("Unable to update location, please try again!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAllLocation")
    public ResponseEntity<List<Location>> getAllLocation() {
        List<Location> locations = locationService.getAllLocation();
        return new ResponseEntity<>(locations, HttpStatus.CREATED);
    }
}
