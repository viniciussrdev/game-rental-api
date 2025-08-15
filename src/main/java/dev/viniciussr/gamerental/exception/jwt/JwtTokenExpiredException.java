package dev.viniciussr.gamerental.exception.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;

/**
 * Exceção lançada quando se tenta validar um token JWT que já expirou.
 */
public class JwtTokenExpiredException extends JWTVerificationException {
    public JwtTokenExpiredException(String message) {
        super(message);
    }
}
