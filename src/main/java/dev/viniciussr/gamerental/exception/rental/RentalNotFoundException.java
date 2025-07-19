package dev.viniciussr.gamerental.exception.rental;

import dev.viniciussr.gamerental.exception.BusinessException;

// Exceção: Aluguel Não Encontrado
public class RentalNotFoundException extends BusinessException {
    public RentalNotFoundException(String message) {
        super(message);
    }
}