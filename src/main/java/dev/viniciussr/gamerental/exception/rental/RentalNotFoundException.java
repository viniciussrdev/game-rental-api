package dev.viniciussr.gamerental.exception.rental;

import dev.viniciussr.gamerental.exception.BusinessException;

/**
 * Exceção lançada quando um aluguel não é encontrado no banco de dados.
 */
public class RentalNotFoundException extends BusinessException {
    public RentalNotFoundException(String message) {
        super(message);
    }
}