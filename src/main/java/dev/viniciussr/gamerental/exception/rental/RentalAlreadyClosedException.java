package dev.viniciussr.gamerental.exception.rental;

import dev.viniciussr.gamerental.exception.BusinessException;

// Exceção: Aluguel Já Devolvido
public class RentalAlreadyClosedException extends BusinessException {
    public RentalAlreadyClosedException(String message) {
        super(message);
    }
}
