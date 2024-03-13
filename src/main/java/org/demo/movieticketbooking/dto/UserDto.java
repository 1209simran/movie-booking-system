package org.demo.movieticketbooking.dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private Long contactNumber;
    private String role;
}
