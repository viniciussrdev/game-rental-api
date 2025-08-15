package dev.viniciussr.gamerental.controller;

import dev.viniciussr.gamerental.dto.UserDto;
import dev.viniciussr.gamerental.dto.UserRegisterDto;
import dev.viniciussr.gamerental.dto.UserUpdateDto;
import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import dev.viniciussr.gamerental.enums.UserRole;
import dev.viniciussr.gamerental.exception.user.UserNotFoundException;
import dev.viniciussr.gamerental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por gerenciar as operações relacionadas a usuários.
 * <p>
 * Disponibiliza endpoints para criação, atualização, exclusão, busca e listagem de usuários,
 * com filtros adicionais por nome, e-mail, função e plano de assinatura.
 * </p>
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint para criar um novo usuário no sistema.
     *
     * @param dto objeto {@link UserRegisterDto} contendo os dados do usuário a ser criado.
     * @return {@link ResponseEntity} com o usuário criado e status {@code 201 Created}.
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserRegisterDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    /**
     * Endpoint para atualizar os dados de um usuário existente.
     *
     * @param id  identificador do usuário a ser atualizado.
     * @param dto objeto {@link UserUpdateDto} contendo os novos dados do usuário.
     * @return {@link ResponseEntity} com o {@link UserDto} do usuário atualizado.
     * @throws UserNotFoundException se o usuário não for encontrado.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDto dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    /**
     * Endpoint para remover um usuário do sistema.
     *
     * @param id identificador do usuário a ser removido.
     * @return {@link ResponseEntity} com status {@code 204 No Content}.
     * @throws UserNotFoundException se o usuário não for encontrado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para buscar um usuário pelo seu identificador.
     *
     * @param id identificador do usuário.
     * @return {@link ResponseEntity} contendo o {@link UserDto} do usuário encontrado.
     * @throws UserNotFoundException se o usuário não for encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    /**
     * Endpoint para listar todos os usuários cadastrados no sistema.
     *
     * @return {@link ResponseEntity} contendo uma lista de {@link UserDto}.
     * @throws UserNotFoundException se nenhum usuário não for encontrado.
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    /**
     * Endpoint para listar usuários filtrados por nome.
     *
     * @param name nome do usuário ou parte dele.
     * @return {@link ResponseEntity} contendo uma lista de {@link UserDto} que correspondem ao nome informado.
     * @throws UserNotFoundException se nenhum usuário não for encontrado.
     */
    @GetMapping(params = "name")
    public ResponseEntity<List<UserDto>> listUsersByName(@RequestParam String name) {
        return ResponseEntity.ok(userService.listUsersByName(name));
    }

    /**
     * Endpoint para listar usuários filtrados por e-mail.
     *
     * @param email e-mail do usuário ou parte dele.
     * @return {@link ResponseEntity} contendo uma lista de {@link UserDto} que correspondem ao e-mail informado.
     * @throws UserNotFoundException se nenhum usuário não for encontrado.
     */
    @GetMapping(params = "email")
    public ResponseEntity<List<UserDto>> listUsersByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.listUsersByEmail(email));
    }

    /**
     * Endpoint para listar usuários filtrados por função (role).
     *
     * @param role função do usuário, conforme o enum {@link UserRole}.
     * @return {@link ResponseEntity} contendo uma lista de {@link UserDto} com a função especificada.
     * @throws UserNotFoundException se nenhum usuário não for encontrado.
     */
    @GetMapping(params = "role")
    public ResponseEntity<List<UserDto>> listUsersByRole(@RequestParam UserRole role) {
        return ResponseEntity.ok(userService.listUsersByRole(role));
    }

    /**
     * Endpoint para listar usuários filtrados por plano de assinatura.
     *
     * @param plan plano de assinatura do usuário, conforme o enum {@link SubscriptionPlans}.
     * @return {@link ResponseEntity} contendo uma lista de {@link UserDto} com o plano especificado.
     * @throws UserNotFoundException se nenhum usuário não for encontrado.
     */
    @GetMapping(params = "plan")
    public ResponseEntity<List<UserDto>> listUsersByPlan(@RequestParam SubscriptionPlans plan) {
        return ResponseEntity.ok(userService.listUsersByPlan(plan));
    }
}