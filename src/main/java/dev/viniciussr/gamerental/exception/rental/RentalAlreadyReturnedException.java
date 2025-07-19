package dev.viniciussr.gamerental.exception.rental;

import dev.viniciussr.gamerental.exception.BusinessException;

// Exceção: Aluguel Já Devolvido
public class RentalAlreadyReturnedException extends BusinessException {
    public RentalAlreadyReturnedException(String message) {
        super(message);
    }
}
