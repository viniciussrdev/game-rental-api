package dev.viniciussr.gamerental.exception.user;

import dev.viniciussr.gamerental.exception.BusinessException;

// Exceção lançada quando um usuário não é encontrado no banco de dados
public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String message) {
        super(message);
    }
}