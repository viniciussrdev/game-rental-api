package dev.viniciussr.gamerental.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        auth -> auth

                                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                                .requestMatchers(HttpMethod.POST, "/games/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/games/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/games/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/games/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/games/**").permitAll()

                                .requestMatchers(HttpMethod.POST, "/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/users/**").permitAll()

                                .requestMatchers(HttpMethod.POST, "/rentals/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/rentals/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/rentals/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/rentals/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/rentals/**").permitAll()

                        .anyRequest().authenticated()
                )
                .build();
    }

    // Gerenciador de autenticação padrão
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




}
