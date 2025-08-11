package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import dev.viniciussr.gamerental.enums.UserRole;

// DTO usado para atualização parcial dos dados de um usuário (PATCH)
// Contém apenas os campos que podem ser atualizados externamente
public record UserUpdateDto(
        String name,
        String email,
        UserRole role,
        String password,
        SubscriptionPlans plan
) {
}