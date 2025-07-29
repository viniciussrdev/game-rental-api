package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import jakarta.validation.constraints.Email;

public record UserUpdateDto(

        String name,

        @Email(message = "Formato de email inválido")
        String email,

        SubscriptionPlans plan
) {
}