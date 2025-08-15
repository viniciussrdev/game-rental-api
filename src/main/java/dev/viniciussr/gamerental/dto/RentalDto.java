package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.RentalStatus;
import dev.viniciussr.gamerental.model.Rental;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

/**
 * DTO utilizado para cadastrar um novo aluguel e retornar seus dados nas respostas da API.
 * <p>
 * Inclui validações para garantir a integridade dos dados fornecidos pelo cliente.
 * </p>
 *
 * @param idRental   identificador único do aluguel.
 * @param gameId     ID do jogo alugado (não pode ser nulo).
 * @param userId     ID do usuário que solicitou o aluguel (não pode ser nulo).
 * @param rentalDate data de início do aluguel (hoje ou no passado).
 * @param endDate    data prevista para encerramento do aluguel (hoje ou no futuro).
 * @param status     status atual do aluguel.
 */
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