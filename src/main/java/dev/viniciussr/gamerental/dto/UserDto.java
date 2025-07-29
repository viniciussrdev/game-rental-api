package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import dev.viniciussr.gamerental.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDto(

        Long idUser,

        @NotBlank(message = "Nome do usuário é campo obrigatório")
        String name,

        @NotBlank(message = "Email do usuário é campo obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotNull(message = "Plano de assinatura é campo obrigatório")
        SubscriptionPlans plan,

        @Min(value = 0, message = "Quantidade mínima de aluguéis ativos: 0 (zero)")
        Integer activeRentals
) {
    public UserDto(User user) {
        this(
                user.getIdUser(),
                user.getName(),
                user.getEmail(),
                user.getPlan(),
                user.getActiveRentals()
        );
    }
}
