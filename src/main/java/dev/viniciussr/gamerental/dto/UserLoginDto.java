package dev.viniciussr.gamerental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado no processo de login/autenticação de um usuário.
 * <p>
 * Recebe as credenciais fornecidas pelo cliente para validação.
 * </p>
 *
 * @param email    endereço de e-mail do usuário (não pode ser nulo ou inválido).
 * @param password senha do usuário (mínimo 6, máximo 20 caracteres).
 */
public record UserLoginDto(

        @NotBlank(message = "Email do usuário é campo obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "Senha do usuário é campo obrigatório")
        @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
        String password
) {
}