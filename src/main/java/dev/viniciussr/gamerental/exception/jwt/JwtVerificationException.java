package dev.viniciussr.gamerental.exception.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;

// Exceção lançada quando há falha na verificação do token JWT
public class JwtVerificationException extends JWTVerificationException {
    public JwtVerificationException(String message) {
        super(message);
    }
}
