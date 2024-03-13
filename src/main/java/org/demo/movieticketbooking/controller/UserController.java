package org.demo.movieticketbooking.controller;

import lombok.extern.slf4j.Slf4j;
import org.demo.movieticketbooking.dto.SignUpDto;
import org.demo.movieticketbooking.dto.UserDto;
import org.demo.movieticketbooking.model.User;
import org.demo.movieticketbooking.service.AuthService;
import org.demo.movieticketbooking.service.UserDetailsServiceImpl;
import org.demo.movieticketbooking.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/signUp")
    public ResponseEntity<?> signupUser(@RequestBody SignUpDto signupDTO) {
        UserDto createdUser = authService.createUser(signupDTO);
        if (createdUser == null) {
            return new ResponseEntity<>("Unable to create user, please try again!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) throws UsernameNotFoundException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),
                    user.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Username or password is incorrect.");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        if (userDetails != null) {
            log.info("Login successfully.");
            return jwtUtil.generateToken(userDetails.getUsername());
        }
        return null;
    }

}