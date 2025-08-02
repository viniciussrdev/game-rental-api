package dev.viniciussr.gamerental.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.viniciussr.gamerental.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {

    @Value( "${jwt.secret}")
    private String secretKey;

    public String generateToken(User user) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT
                    .create()
                    .withIssuer("Game Rental API")
                    .withSubject(user.getEmail())
                    .withExpiresAt(getTokenExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWT.require(algorithm).withIssuer("Game Rental API").build().verify(token).getSubject();
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    public Instant getTokenExpirationDate() {
        return Instant.now().plus(2, ChronoUnit.HOURS);

    }
}
