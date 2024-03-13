package org.demo.movieticketbooking.dto;

import lombok.Data;

@Data
public class SignUpDto {

    private String name;
    private String email;
    private String password;
    private Long contactNumber;

}