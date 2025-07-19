package dev.viniciussr.gamerental.exception.user;

import dev.viniciussr.gamerental.exception.BusinessException;

// Exceção: Usuário Não Encontrado
public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String message) {
        super(message);
    }
}