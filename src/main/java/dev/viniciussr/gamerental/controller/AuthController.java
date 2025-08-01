package dev.viniciussr.gamerental.controller;

import dev.viniciussr.gamerental.dto.UserDto;
import dev.viniciussr.gamerental.dto.UserLoginDto;
import dev.viniciussr.gamerental.dto.UserRegisterDto;
import dev.viniciussr.gamerental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    // Endpoint para cadastro de novo usuário
    @PostMapping("/register")
    public ResponseEntity<UserDto> register (@RequestBody @Valid UserRegisterDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    // Endpoint para efetuação de login do usuário
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDto dto) {

        UsernamePasswordAuthenticationToken usernamePassword  =
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.password()
                );

        Authentication authentication = authenticationManager.authenticate(usernamePassword);

        return ResponseEntity.ok().build();
    }










}
