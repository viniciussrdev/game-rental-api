package dev.viniciussr.gamerental.exception;

// Classe base para exceções personalizadas relacionadas a regras de negócio
// Estende RuntimeException, permitindo que seja lançada sem necessidade de tratamento obrigatório
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}