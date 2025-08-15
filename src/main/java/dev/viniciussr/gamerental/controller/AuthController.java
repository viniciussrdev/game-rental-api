package dev.viniciussr.gamerental.controller;

import dev.viniciussr.gamerental.dto.TokenDto;
import dev.viniciussr.gamerental.dto.UserDto;
import dev.viniciussr.gamerental.dto.UserLoginDto;
import dev.viniciussr.gamerental.dto.UserRegisterDto;
import dev.viniciussr.gamerental.exception.jwt.JwtGenerationException;
import dev.viniciussr.gamerental.service.LoginService;
import dev.viniciussr.gamerental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador responsável por operações de autenticação e registro de usuários.
 * <p>
 * Disponibiliza endpoints para criar uma nova conta de usuário e realizar login,
 * retornando um token JWT para autenticação nas requisições subsequentes.
 * </p>
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final LoginService loginService;

    public AuthController(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    /**
     * Endpoint para registrar um novo usuário no sistema.
     * <p>
     * Recebe os dados de cadastro via {@link UserRegisterDto} e cria um novo usuário
     * persistindo-o no banco de dados.
     * </p>
     *
     * @param dto objeto {@link UserRegisterDto} contendo os dados necessários para criação do usuário.
     * @return {@link ResponseEntity} contendo o {@link UserDto} do usuário criado e status {@code 201 Created}.
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> register (@RequestBody @Valid UserRegisterDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    /**
     * Endpoint para realizar a autenticação de um usuário.
     * <p>
     * Valida as credenciais enviadas e, se corretas, retorna um token JWT que deverá ser utilizado
     * nas requisições protegidas da API.
     * </p>
     *
     * @param dto objeto {@link UserLoginDto} contendo as credenciais do usuário.
     * @return {@link ResponseEntity} contendo um {@link TokenDto} com o token JWT gerado.
     * @throws AuthenticationException se as credenciais não corresponderem a um usuário válido.
     * @throws JwtGenerationException se ocorrer erro ao gerar o token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid UserLoginDto dto) {
        String token = loginService.login(dto);
        return ResponseEntity.ok(new TokenDto(token));
    }
}