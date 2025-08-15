package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.GameGenres;
import dev.viniciussr.gamerental.enums.Platforms;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.Set;

/**
 * DTO utilizado para atualização parcial dos dados de um jogo (requisição PATCH).
 * <p>
 * Contém apenas os campos que podem ser atualizados externamente.
 * </p>
 *
 * @param title    novo título do jogo.
 * @param genre    novo gênero do jogo.
 * @param platform novas plataformas em que o jogo estará disponível.
 * @param quantity nova quantidade disponível em estoque (mínimo 1, máximo 100).
 */
public record GameUpdateDto(

        String title,

        GameGenres genre,

        Set<Platforms> platform,

        @Min(value = 1, message = "Quantidade mínima: 01 (um)")
        @Max(value = 100, message = "Quantidade máxima: 100 (cem)")
        Integer quantity
) {
}