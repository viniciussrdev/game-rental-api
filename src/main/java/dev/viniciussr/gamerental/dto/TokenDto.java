package dev.viniciussr.gamerental.dto;

/**
 * DTO utilizado para retornar um token de acesso (JWT) para o cliente.
 *
 * @param token token JWT gerado para autenticação e autorização.
 */
public record TokenDto(String token) {
}