package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.GameGenres;
import dev.viniciussr.gamerental.enums.Platforms;
import dev.viniciussr.gamerental.model.Game;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.Set;

public record GameUpdateDto(

        String title,

        GameGenres genre,

        Set<Platforms> platform,

        @Min(value = 1, message = "Quantidade mínima: 01 (um)")
        @Max(value = 100, message = "Quantidade máxima: 100 (cem)")
        Integer quantity
) {
}