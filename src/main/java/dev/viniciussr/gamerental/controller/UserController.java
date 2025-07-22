package dev.viniciussr.gamerental.controller;

import dev.viniciussr.gamerental.dto.UserDto;
import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import dev.viniciussr.gamerental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users") // Rota base para as requisições deste controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint para criar novo usuário
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    // Endpoint para atualizar usuário existente
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    // Endpoint para deletar usuário do banco de dados
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    // Endpoint para listar todos os usuários
    @GetMapping
    public ResponseEntity<List<UserDto>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    // Endpoint para listar usuários por nome
    @GetMapping(params = "name")
    public ResponseEntity<List<UserDto>> listUsersByName(@RequestParam String name) {
        return ResponseEntity.ok(userService.listUsersByName(name));
    }

    // Endpoint para listar usuários por email
    @GetMapping(params = "email")
    public ResponseEntity<List<UserDto>> listUsersByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.listUsersByEmail(email));
    }

    // Endpoint para listar usuários por plano
    @GetMapping(params = "plan")
    public ResponseEntity<List<UserDto>> listUsersByPlan(@RequestParam SubscriptionPlans plan) {
        return ResponseEntity.ok(userService.listUsersByPlan(plan));
    }
}