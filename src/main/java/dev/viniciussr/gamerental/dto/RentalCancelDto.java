package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.RentalStatus;
import dev.viniciussr.gamerental.model.Rental;

import java.time.LocalDate;

public record RentalCancelDto(

        Long idRental,

        Long gameId,

        Long userId,

        LocalDate rentalDate,

        LocalDate cancelDate,

        RentalStatus status
) {
    public RentalCancelDto(Rental rental) {
        this(
                rental.getIdRental(),
                rental.getGame().getIdGame(),
                rental.getUser().getIdUser(),
                rental.getRentalDate(),
                rental.getReturnDate(), // Campo returnDate usado como cancelDate no JSON de resposta
                rental.getStatus()
        );
    }
}