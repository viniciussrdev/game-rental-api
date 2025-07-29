package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.RentalStatus;

public record RentalUpdateDto(

        Long gameId,

        Long userId,

        RentalStatus status
) {
}
