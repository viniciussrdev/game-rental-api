package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.RentalStatus;
import dev.viniciussr.gamerental.model.Rental;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RentalDto(

        Long idRental,

        @NotNull(message = "ID do jogo é campo obrigatório")
        Long gameId,

        @NotNull(message = "ID do usuário é campo obrigatório")
        Long userId,

        @NotNull(message = "Data do aluguel é campo obrigatório")
        @PastOrPresent(message = "A data do aluguel deve ser hoje ou uma data passada")
        LocalDate rentalDate,

        @NotNull(message = "Data de devolução é campo obrigatório")
        @FutureOrPresent(message = "A data de devolução deve ser hoje ou uma data futura")
        LocalDate returnDate,

        @NotNull(message = "Status do aluguel é campo obrigatório")
        RentalStatus status
) {
    public RentalDto(Rental rental) {
        this(
                rental.getIdRental(),
                rental.getGame().getIdGame(),
                rental.getUser().getIdUser(),
                rental.getRentalDate(),
                rental.getReturnDate(),
                rental.getStatus()
        );
    }
}
