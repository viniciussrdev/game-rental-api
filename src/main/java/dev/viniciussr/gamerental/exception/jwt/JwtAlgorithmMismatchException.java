package dev.viniciussr.gamerental.exception.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;

/**
 * Exceção lançada quando se tenta validar um token JWT com algoritmo inválido.
 */
public class JwtAlgorithmMismatchException extends JWTVerificationException {
    public JwtAlgorithmMismatchException(String message) {
        super(message);
    }
}