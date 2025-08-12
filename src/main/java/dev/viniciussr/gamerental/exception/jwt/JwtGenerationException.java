package dev.viniciussr.gamerental.exception.jwt;

// Exceção lançada quando há erro ao gerar um token JWT
public class JwtGenerationException extends RuntimeException {
    public JwtGenerationException(String message) {
        super(message);
    }
}
