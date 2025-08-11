package dev.viniciussr.gamerental.dto;

// DTO usado para atualização parcial dos dados de um aluguel (PATCH)
// Contém apenas os campos que podem ser atualizados externamente
public record RentalUpdateDto(
        Long gameId,
        Long userId
) {
}