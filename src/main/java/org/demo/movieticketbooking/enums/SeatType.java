package org.demo.movieticketbooking.enums;

public enum SeatType {

    PREMIUM("Premium"),
    EXECUTIVE("Executive"),
    CLUB("Club"),
    ROYALE("Royale");

    private final String type;

    SeatType(String type) {
        this.type = type;
    }
}