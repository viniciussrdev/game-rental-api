package dev.viniciussr.gamerental.service;

import dev.viniciussr.gamerental.dto.UserDto;
import dev.viniciussr.gamerental.dto.UserRegisterDto;
import dev.viniciussr.gamerental.dto.UserUpdateDto;
import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import dev.viniciussr.gamerental.enums.UserRole;
import dev.viniciussr.gamerental.exception.rental.PlanLimitExceededException;
import dev.viniciussr.gamerental.exception.user.UserNotFoundException;
import dev.viniciussr.gamerental.model.User;
import dev.viniciussr.gamerental.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

// Serviço responsável por gerenciar operações relacionadas aos usuários da aplicação
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ******************************
    // CRUD BÁSICO
    // ******************************

    // Cria um novo usuário
    public UserDto createUser(UserRegisterDto dto) {

        String encryptedPassword = passwordEncoder.encode(dto.password()); // Criptografa a senha

        User user = new User(
                dto.name(),
                dto.email(),
                encryptedPassword, // Define a senha criptografada
                UserRole.USER, // Usuário é criado com role padrão 'USER'
                dto.plan(),
                0 // Usuário é criado com zero aluguéis ativos
        );
        return new UserDto(userRepository.save(user));
    }

    // Atualiza um usuário existente
    public UserDto updateUser(Long id, UserUpdateDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado no id: " + id));

        // Atualização parcial (PATCH)
        // Se algum atributo chegar nulo, não ocorrerá a atualização do campo
        if (dto.name()     != null) user.setName(dto.name());
        if (dto.email()    != null) user.setEmail(dto.email());
        if (dto.password() != null) user.setPassword(passwordEncoder.encode(dto.password()));
        if (dto.role()     != null) user.setRole(dto.role());
        if (dto.plan()     != null) user.setPlan(dto.plan());

        return new UserDto(userRepository.save(user));
    }

    // Deleta um usuário existente
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado no id: " + id));

        userRepository.delete(user);
    }

    // ******************************
    // MÉTODOS DE BUSCA
    // ******************************

    // Busca um usuário pelo ID
    public UserDto findUserById(Long id) {

        return userRepository.findById(id)
                .map(UserDto::new)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado no id: " + id));

    }

    // Lista todos os usuários cadastrados
    public List<UserDto> listUsers() {

        List<UserDto> users = userRepository.findAll()
                .stream()
                .map(UserDto::new)
                .toList();
        if (users.isEmpty()) {
            throw new UserNotFoundException("Nenhum usuário cadastrado no momento");
        }
        return users;
    }

    // Lista usuários por nome
    public List<UserDto> listUsersByName(String name) {

        List<UserDto> users = userRepository
                .findByNameContainingIgnoreCase(name) // Busca ocorrências parciais e ignora maiúsculas/minúsculas
                .stream()
                .map(UserDto::new)
                .toList();
        if (users.isEmpty()) {
            throw new UserNotFoundException("Nenhum Usuário encontrado com o Nome: " + name);
        }
        return users;
    }

    // Lista usuários por email
    public List<UserDto> listUsersByEmail(String email) {

        List<UserDto> users = userRepository
                .findByEmailContainingIgnoreCase(email) // Busca ocorrências parciais e ignora maiúsculas/minúsculas
                .stream()
                .map(UserDto::new)
                .toList();
        if (users.isEmpty()) {
            throw new UserNotFoundException("Nenhum Usuário encontrado com o Email: " + email);
        }
        return users;
    }

    // Lista usuários por role
    public List<UserDto> listUsersByRole(UserRole role) {

        List<UserDto> users = userRepository.findByRole(role)
                .stream()
                .map(UserDto::new)
                .toList();
        if (users.isEmpty()) {
            throw new UserNotFoundException("Nenhum Usuário encontrado com a Role: " + role.name());
        }
        return users;
    }

    // Lista usuários por plano
    public List<UserDto> listUsersByPlan(SubscriptionPlans plan) {

        List<UserDto> users = userRepository.findByPlan(plan)
                .stream()
                .map(UserDto::new)
                .toList();
        if (users.isEmpty()) {
            throw new UserNotFoundException("Nenhum Usuário encontrado com o Plano: " + plan.name());
        }
        return users;
    }

    // ******************************
    // LÓGICA DE NEGÓCIO
    // ******************************

    // Valida se o limite de aluguéis ativos do usuário foi excedido
    void validateUserRentalLimit(User user) {

        int maxRentals = switch (user.getPlan()) { // Busca o plano do usuário
            case NOOB -> 1;   // Permite 1 (um) aluguel por vez
            case PRO -> 3;    // Permite 3 (três) aluguéis por vez
            case LEGEND -> 5; // Permite 5 (cinco) aluguéis por vez
        };
        // Verifica se nº de aluguéis ativos é maior ou iguail ao limite
        if (user.getActiveRentals() >= maxRentals) {
            throw new PlanLimitExceededException(user);
        }
    }

    // Atualiza contagem de alugués ativos do usuário
    void updateUserActiveRentalsCount(User user, int x) {

        // Novo aluguel (x = 1) → quantidade AUMENTA 1; Devolução (x = -1) → quantidade DIMINUI 1
        user.setActiveRentals(user.getActiveRentals() + x);

        userRepository.save(user);
    }

    // ******************************
    // USER DETAILS SERVICE
    // ******************************

    // Busca um usuário por email
    // Permite que o Spring Security carregue os dados do usuário durante o processo de autenticação
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}