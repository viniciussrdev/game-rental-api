package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.RentalStatus;
import dev.viniciussr.gamerental.model.Rental;

public record RentalUpdateDto(

        Long gameId,

        Long userId,

        RentalStatus status
) {
}
