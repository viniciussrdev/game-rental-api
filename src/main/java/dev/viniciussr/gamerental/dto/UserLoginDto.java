package dev.viniciussr.gamerental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// DTO usado no processo de login/autenticação de um usuário
// Recebe as credenciais fornecidas pelo cliente para validação
public record UserLoginDto(

        @NotBlank(message = "Email do usuário é campo obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "Senha do usuário é campo obrigatório")
        @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
        String password
) {
}