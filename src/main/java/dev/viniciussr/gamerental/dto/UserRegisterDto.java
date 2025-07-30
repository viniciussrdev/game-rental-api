package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import dev.viniciussr.gamerental.enums.UserRole;
import jakarta.validation.constraints.*;

public record UserRegisterDto(

        Long idUser,

        @NotBlank(message = "Nome do usuário é campo obrigatório")
        String name,

        @NotBlank(message = "Email do usuário é campo obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "Senha do usuário é campo obrigatório")
        @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
        String password,

        @NotNull(message = "Tipo de usuário é campo obrigatório")
        UserRole role,

        @NotNull(message = "Plano de assinatura é campo obrigatório")
        SubscriptionPlans plan,

        @Min(value = 0, message = "Quantidade mínima de aluguéis ativos: 0 (zero)")
        Integer activeRentals
) {
}
