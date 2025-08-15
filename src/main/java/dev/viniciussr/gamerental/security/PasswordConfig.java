package dev.viniciussr.gamerental.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuração de senha da API.
 */
@Configuration
public class PasswordConfig {

    /**
     * Criptografa senhas utilizando o algoritmo BCrypt.
     *
     * @return PasswordEncoder configurado com BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}