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

/**
 * Configuração de segurança da API.
 * <p>
 * Define regras de autenticação, autorização e política de sessão 'STATELESS'.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    /**
     * Configura a cadeia de filtros de segurança do Spring Security.
     * <p>
     * Define CSRF, gerenciamento de sessão, regras de autorização por endpoint e adiciona o filtro JWT.
     * </p>
     *
     * @param http Configuração HTTP do Spring Security.
     * @return SecurityFilterChain configurado.
     * @throws Exception Lança exceção caso ocorra erro na configuração de segurança.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http

                // Desabilita proteção CSRF (JWT já garante a segurança)
                .csrf(AbstractHttpConfigurer::disable)

                // Define política de sessão sem estado (STATELESS)
                .sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Define as regras de autorização para os endpoints da API
                .authorizeHttpRequests(
                        auth -> auth

                                // Endpoints públicos: acesso livre para cadastro e login
                                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                                // Controle de acesso para endpoints relacionados a jogos
                                .requestMatchers(HttpMethod.POST, "/games/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/games/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/games/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/games/**").hasAnyRole("ADMIN", "USER")

                                // Controle de acesso para endpoints relacionados a usuários
                                .requestMatchers(HttpMethod.POST, "/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")

                                // Controle de acesso para endpoints relacionados a aluguéis
                                .requestMatchers(HttpMethod.POST, "/rentals/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/rentals/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/rentals/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/rentals/**").hasRole("ADMIN")

                                // Permite acesso aos endpoints de documentação (Swagger/OpenAPI)
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**"
                                ).permitAll()

                                // Qualquer outra requisição exige autenticação
                                .anyRequest().authenticated()
                )

                // Adiciona o filtro JWT antes do filtro padrão de autenticação
                .addFilterBefore(
                        jwtTokenFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    /**
     * Define o gerenciador de autenticação do Spring Security.
     *
     * @param config Configuração de autenticação.
     * @return AuthenticationManager configurado.
     * @throws Exception Lança exceção caso ocorra erro ao obter AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}