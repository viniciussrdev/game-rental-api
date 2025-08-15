package dev.viniciussr.gamerental.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dev.viniciussr.gamerental.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Serviço responsável por gerar, validar e definir a expiração de tokens JWT.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey; // Chave secreta usada na assinatura do token

    /**
     * Configura o algoritmo de assinatura do token JWT usando a chave secreta.
     *
     * @return Algoritmo de assinatura HMAC256.
     */
    private Algorithm algorithm() {
        return Algorithm.HMAC256(secretKey);
    }

    /**
     * Gera um token JWT para o usuário fornecido.
     *
     * @param user usuário para quem o token será gerado.
     * @return Token JWT assinado.
     */
    public String generateToken(User user) {
        return JWT.create()
                .withIssuer("Game Rental API")
                .withSubject(user.getEmail())
                .withExpiresAt(getTokenExpirationDate())
                .sign(algorithm());
    }

    /**
     * Valida um token JWT.
     * <p>
     * Verifica assinatura e emissor do token.
     * </p>
     *
     * @param token token JWT a ser validado.
     */
    public void validateToken(String token) {
        JWT.require(algorithm())
                .withIssuer("Game Rental API")
                .build()
                .verify(token);
    }

    /**
     * Retorna a data/hora de expiração do token JWT.
     *
     * @return Data/hora em que o token expira.
     */
    public Instant getTokenExpirationDate() {
        return Instant.now().plus(2, ChronoUnit.HOURS); // Validade: 2 horas
    }
}