package org.demo.movieticketbooking.service;

import lombok.extern.slf4j.Slf4j;
import org.demo.movieticketbooking.dto.LocationDto;
import org.demo.movieticketbooking.model.Location;
import org.demo.movieticketbooking.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public Location addLocation(LocationDto locationDto) {
        try {
            Location location = new Location();
            location.setCity(locationDto.getCity());
            location.setCountry(locationDto.getCountry());
            location.setState(locationDto.getState());
            locationRepository.save(location);
            return location;
        } catch (Throwable ex) {
            log.error("Error in saving location: {}", locationDto);
        }
        return null;
    }

    public Location updateLocation(Location location) {
        try {
            Optional<Location> locationNew = locationRepository.findById(location.getId());
            List<Location> loc = locationNew.stream().toList();
            if (loc.size() == 0) {
                log.info("No location found for id: {}", location.getId());
                return null;
            }
            for (Location locn : loc) {
                locn.setCity(location.getCity());
                locn.setCountry(location.getCountry());
                locn.setState(location.getState());
                locationRepository.save(locn);
            }
            log.info("Updated ");
            return location;
        } catch (Throwable ex) {
            log.error("Error in updating location id: {}", location.getId());
        }
        return null;
    }

    @Override
    public List<Location> getAllLocation() {
        List<Location> locations = new ArrayList<>();
        locations.addAll(locationRepository.findAll());
        return locations;
    }


}
