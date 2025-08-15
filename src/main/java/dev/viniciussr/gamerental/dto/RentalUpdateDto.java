package dev.viniciussr.gamerental.dto;

/**
 * DTO utilizado para atualização parcial dos dados de um aluguel (requisição PATCH).
 * <p>
 * Contém apenas os campos que podem ser atualizados externamente.
 * </p>
 *
 * @param gameId ID correto do jogo alugado.
 * @param userId ID correto do usuário que solicitou o aluguel.
 */
public record RentalUpdateDto(
        Long gameId,
        Long userId
) {
}