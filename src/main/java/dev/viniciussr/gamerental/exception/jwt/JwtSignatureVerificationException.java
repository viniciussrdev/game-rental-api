package dev.viniciussr.gamerental.exception.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;

// Exceção lançada quando se tenta validar um token JWT com assinatura inválida
public class JwtSignatureVerificationException extends JWTVerificationException {
    public JwtSignatureVerificationException(String message) {
        super(message);
    }
}
