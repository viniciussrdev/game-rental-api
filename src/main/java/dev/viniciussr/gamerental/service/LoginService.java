package dev.viniciussr.gamerental.service;

import dev.viniciussr.gamerental.dto.UserLoginDto;
import dev.viniciussr.gamerental.exception.jwt.JwtGenerationException;
import dev.viniciussr.gamerental.model.User;
import dev.viniciussr.gamerental.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

// Serviço responsável por autenticar um usuário na API
@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public String login(UserLoginDto dto) {

        // Cria o authToken com as credenciais recebidas do usuário
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.password()
                );

        Authentication auth = authenticationManager
                .authenticate(authToken); // Autentica o usuário no Spring Security

        User user = (User) auth.getPrincipal(); // Recupera o usuário autenticado
        
        try {
            return jwtService.generateToken(user); // Gera e retorna o token JWT para o usuário
        }  catch (RuntimeException e) {
            throw new JwtGenerationException("Erro ao gerar token JWT");
        }
    }
}