package org.demo.movieticketbooking.service;

import org.demo.movieticketbooking.dto.LocationDto;
import org.demo.movieticketbooking.model.Location;

import java.util.List;

public interface LocationService {

    Location addLocation(LocationDto locationDto);

    Location updateLocation(Location location);

    List<Location> getAllLocation();
}
