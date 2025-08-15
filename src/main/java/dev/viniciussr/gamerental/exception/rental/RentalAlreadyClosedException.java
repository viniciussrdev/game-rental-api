package dev.viniciussr.gamerental.exception.rental;

import dev.viniciussr.gamerental.exception.BusinessException;

/**
 * Exceção lançada quando se tenta encerrar um aluguel já previamente encerrado.
 */
public class RentalAlreadyClosedException extends BusinessException {
    public RentalAlreadyClosedException(String message) {
        super(message);
    }
}