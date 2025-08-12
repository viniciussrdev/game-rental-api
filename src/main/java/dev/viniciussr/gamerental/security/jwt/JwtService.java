package dev.viniciussr.gamerental.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dev.viniciussr.gamerental.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

// Serviço responsável por gerar, validar e definir a expiração de tokens JWT
@Service
public class JwtService {

    @Value( "${jwt.secret}") // Injeta o valor da chave secreta definida no application.properties
    private String secretKey;

    // Configura o algoritmo de assinatura do token usando a chave secreta
    private Algorithm algorithm() {
        return Algorithm.HMAC256(secretKey);
    }

    // Gera um token JWT
    public String generateToken(User user) {

        return JWT
                .create()
                .withIssuer("Game Rental API")            // Define o emissor do token
                .withSubject(user.getEmail())             // Define o usuário "dono" do token
                .withExpiresAt(getTokenExpirationDate())  // Define a data de expiração do token
                .sign(algorithm());                       // Assina com a chave secreta
    }

    // Valida um token JWT
    // Verifica assinatura e emissor
    public void validateToken(String token) {

        JWT
                .require(algorithm())
                .withIssuer("Game Rental API")
                .build()
                .verify(token);
    }

    // Retorna a data/hora de expiração do token JWT
    public Instant getTokenExpirationDate() {

        return Instant.now().plus(2, ChronoUnit.HOURS); // Validade: 2 horas
    }
}