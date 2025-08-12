package dev.viniciussr.gamerental.security;

import dev.viniciussr.gamerental.security.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Classe de configuração de segurança da API
// Define regras de autorização, autenticação, política 'STATELESS'
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    // Define a cadeia de filtros de segurança com as regras de autorização/autenticação da API
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        return http
                .csrf(AbstractHttpConfigurer::disable) // Desabilita proteção CSRF (já que usamos JWT)
                .sessionManagement(
                        session -> session
                                // Política de sessão 'STATELESS' (não armazena o estado da autenticação no servidor)
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Regras de autorização para os endpoints da API
                .authorizeHttpRequests(
                        auth -> auth

                                // Acesso irrestrito para realização de cadastro e login
                                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                                // Gerenciamento de jogos restrito aos ADMINs
                                // USERs podem listar/buscar jogos
                                .requestMatchers(HttpMethod.POST, "/games/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/games/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/games/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/games/**").hasAnyRole("ADMIN", "USER")

                                // Gerenciamento de usuários restrito aos ADMINs
                                .requestMatchers(HttpMethod.POST, "/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")

                                // Gerenciamento de aluguéis restrito aos ADMINs
                                .requestMatchers(HttpMethod.POST, "/rentals/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/rentals/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/rentals/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/rentals/**").hasRole("ADMIN")

                                // Qualquer outra requisição exige autenticação
                                .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtTokenFilter, // Filtro de verificação do token JWT
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    // Define o gerenciador de autenticação do Spring Security
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}