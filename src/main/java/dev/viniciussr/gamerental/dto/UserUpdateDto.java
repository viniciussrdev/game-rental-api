package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import dev.viniciussr.gamerental.enums.UserRole;

/**
 * DTO utilizado para atualização parcial dos dados de um usuário (requisição PATCH).
 * <p>
 * Contém apenas os campos que podem ser atualizados externamente.
 * </p>
 *
 * @param name     novo nome do usuário.
 * @param email    novo e-mail do usuário.
 * @param role     nova função (role) atribuída ao usuário.
 * @param password nova senha do usuário.
 * @param plan     novo plano de assinatura.
 */
public record UserUpdateDto(
        String name,
        String email,
        UserRole role,
        String password,
        SubscriptionPlans plan
) {
}