package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import dev.viniciussr.gamerental.enums.UserRole;
import dev.viniciussr.gamerental.model.User;

/**
 * DTO utilizado para retornar os dados de um usuário nas respostas da API.
 * <p>
 * Representa um usuário já cadastrado, sem expor a senha.
 * </p>
 *
 * @param idUser        identificador único do usuário.
 * @param name          nome completo do usuário.
 * @param email         endereço de e-mail do usuário.
 * @param role          função (role) do usuário no sistema.
 * @param plan          plano de assinatura do usuário.
 * @param activeRentals quantidade de aluguéis ativos do usuário.
 */
public record UserDto(
        Long idUser,
        String name,
        String email,
        UserRole role,
        SubscriptionPlans plan,
        Integer activeRentals
) {
    public UserDto(User user) {
        this(
                user.getIdUser(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getPlan(),
                user.getActiveRentals()
        );
    }
}