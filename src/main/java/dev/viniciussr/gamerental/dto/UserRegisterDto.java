package dev.viniciussr.gamerental.dto;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para cadastrar um novo usuário.
 * <p>
 * Inclui validações para garantir a integridade dos dados fornecidos pelo cliente.
 * </p>
 *
 * @param name     nome completo do usuário a ser cadastrado.
 * @param email    endereço de e-mail do usuário a ser cadastrado.
 * @param password senha do usuário a ser cadastrado (mínimo 6, máximo 20 caracteres).
 * @param plan     plano de assinatura escolhido pelo usuário a ser cadastrado (não pode ser nulo).
 */
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