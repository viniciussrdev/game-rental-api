package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import dev.viniciussr.gamerental.enums.UserRole;
import dev.viniciussr.gamerental.model.User;

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
