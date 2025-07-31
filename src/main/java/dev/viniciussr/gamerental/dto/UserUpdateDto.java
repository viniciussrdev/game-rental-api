package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import dev.viniciussr.gamerental.enums.UserRole;

public record UserUpdateDto(
        String name,
        String email,
        UserRole role,
        String password,
        SubscriptionPlans plan
) {
}