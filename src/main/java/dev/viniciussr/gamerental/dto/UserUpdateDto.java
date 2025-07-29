package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import dev.viniciussr.gamerental.model.User;
import jakarta.validation.constraints.Email;

public record UserUpdateDto(

        String name,

        @Email(message = "Formato de email inválido")
        String email,

        SubscriptionPlans plan
) {
    public UserUpdateDto(User user) {
        this(
                user.getName(),
                user.getEmail(),
                user.getPlan()
        );
    }
}