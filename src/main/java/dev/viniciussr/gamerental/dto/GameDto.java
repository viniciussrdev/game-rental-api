package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.GameGenres;
import dev.viniciussr.gamerental.enums.Platforms;
import dev.viniciussr.gamerental.model.Game;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * DTO utilizado para cadastrar um novo jogo e retornar seus dados nas respostas da API.
 * <p>
 * Inclui validações para garantir a integridade dos dados fornecidos pelo cliente.
 * </p>
 *
 * @param idGame    identificador único do jogo.
 * @param title     título do jogo (não pode estar em branco).
 * @param genre     gênero do jogo (não pode ser nulo).
 * @param platform  conjunto de plataformas em que o jogo está disponível (não pode ser nulo).
 * @param quantity  quantidade disponível em estoque (mínimo 1, máximo 100).
 * @param available indica se o jogo está disponível para aluguel.
 */
public record GameDto(

        Long idGame,

        @NotBlank(message = "Título do jogo é campo obrigatório")
        String title,

        @NotNull(message = "Gênero do jogo é campo obrigatório")
        GameGenres genre,

        @NotNull(message = "Plataforma(s) do jogo é campo obrigatório")
        Set<Platforms> platform,

        @NotNull(message = "Quantidade do jogo é campo obrigatório")
        @Min(value = 1, message = "Quantidade mínima: 01 (um)")
        @Max(value = 100, message = "Quantidade máxima: 100 (cem)")
        Integer quantity,

        boolean available
) {
    public GameDto(Game game) {
        this(
                game.getIdGame(),
                game.getTitle(),
                game.getGenre(),
                game.getPlatform(),
                game.getQuantity(),
                game.isAvailable()
        );
    }
}