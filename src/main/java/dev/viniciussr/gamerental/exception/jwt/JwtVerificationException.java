package dev.viniciussr.gamerental.exception.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;

/**
 * Exceção lançada quando ocorre falha genérica na verificação de um token JWT.
 */
public class JwtVerificationException extends JWTVerificationException {
    public JwtVerificationException(String message) {
        super(message);
    }
}