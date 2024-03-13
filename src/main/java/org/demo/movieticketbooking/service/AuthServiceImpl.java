package org.demo.movieticketbooking.service;

import org.demo.movieticketbooking.constants.ErrorConstant;
import org.demo.movieticketbooking.dto.SignUpDto;
import org.demo.movieticketbooking.dto.UserDto;
import org.demo.movieticketbooking.enums.UserRoles;
import org.demo.movieticketbooking.model.User;
import org.demo.movieticketbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto createUser(SignUpDto signupDTO) {
        User user = new User();
        user.setEmail(signupDTO.getEmail());
        user.setName(signupDTO.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupDTO.getPassword()));
        user.setContactNumber(signupDTO.getContactNumber());
        user.setRole(UserRoles.ROLE_USER.toString());
        userRepository.save(user);
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setId(user.getId());
        userDto.setContactNumber(user.getContactNumber());
        userDto.setRole(user.getRole());
        return userDto;

    }

    @Override
    public boolean validUser(Long userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            List<User> users = user.stream().toList();
            return !users.isEmpty();
        } catch (Throwable ex) {
            throw new RuntimeException(ErrorConstant.USER_NOT_FOUND);
        }
    }
}
