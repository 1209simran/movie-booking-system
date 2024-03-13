package org.demo.movieticketbooking.service;

import org.demo.movieticketbooking.dto.SignUpDto;
import org.demo.movieticketbooking.dto.UserDto;

public interface AuthService {
    UserDto createUser(SignUpDto signupDTO);

    boolean validUser(Long userId);
}
