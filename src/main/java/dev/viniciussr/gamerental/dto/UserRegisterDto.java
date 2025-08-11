package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// DTO usado para cadastrar um novo usuário
// Inclui validações para garantia da integridade dos dados fornecidos pelo cliente
public record UserRegisterDto(

        @NotBlank(message = "Nome do usuário é campo obrigatório")
        String name,

        @NotBlank(message = "Email do usuário é campo obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "Senha do usuário é campo obrigatório")
        @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
        String password,

        @NotNull(message = "Plano de assinatura é campo obrigatório")
        SubscriptionPlans plan
) {
}
