package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.GameGenres;
import dev.viniciussr.gamerental.enums.Platforms;

import java.util.Set;

public record GameUpdateDto(
        String title,
        GameGenres genre,
        Set<Platforms> platform,
        Integer quantity
) {
}