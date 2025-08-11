package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.RentalStatus;
import dev.viniciussr.gamerental.model.Rental;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

// DTO usado para cadastrar um novo aluguel e retornar seus dados nas respostas da API
// Inclui validações para garantia da integridade dos dados fornecidos pelo cliente
public record RentalDto(

        Long idRental,

        @NotNull(message = "ID do jogo é campo obrigatório")
        Long gameId,

        @NotNull(message = "ID do usuário é campo obrigatório")
        Long userId,

        @PastOrPresent(message = "A data do aluguel deve ser hoje ou uma data passada")
        LocalDate rentalDate,

        @FutureOrPresent(message = "A data de encerramento deve ser hoje ou uma data futura")
        LocalDate endDate,

        RentalStatus status
) {
    public RentalDto(Rental rental) {
        this(
                rental.getIdRental(),
                rental.getGame().getIdGame(),
                rental.getUser().getIdUser(),
                rental.getRentalDate(),
                rental.getEndDate(),
                rental.getStatus()
        );
    }
}