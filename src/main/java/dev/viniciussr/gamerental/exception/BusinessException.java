package dev.viniciussr.gamerental.exception;

/**
 * Classe base para exceções personalizadas relacionadas a regras de negócio.
 * <p>
 * Estende {@link RuntimeException}, permitindo que seja lançada sem necessidade
 * de tratamento obrigatório.
 * </p>
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}