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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ********** USER DETAILS SERVICE **********

    // Busca um usuário por username (email)
    // Exigido pela interface UserDetailsService
    // Permite que o Spring Security carregue os dados do usuário durante o processo de autenticação
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    // *************** CRUD BÁSICO ***************

    // Cria um novo usuário
    public UserDto createUser(UserRegisterDto dto) {

        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());

        User user = new User(
                dto.name(),
                dto.email(),
                dto.password(),
                UserRole.USER,
                dto.plan(),
                0
        );
        user.setPassword(encryptedPassword);

        return new UserDto(userRepository.save(user));
    }

    // Atualiza um usuário existente
    public UserDto updateUser(Long id, UserUpdateDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado no id: " + id));

        if (dto.name()     != null) user.setName(dto.name());
        if (dto.email()    != null) user.setEmail(dto.email());
        if (dto.password() != null) user.setPassword(dto.password());
        if (dto.role()     != null) user.setRole(dto.role());
        if (dto.plan()     != null) user.setPlan(dto.plan());

        return new UserDto(userRepository.save(user));
    }

    // Remove um usuário existente
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado no id: " + id));

        userRepository.delete(user);
    }

    // --------------- FILTROS ---------------

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

        List<UserDto> users = userRepository.findByNameContainingIgnoreCase(name)
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

        List<UserDto> users = userRepository.findByEmailContainingIgnoreCase(email)
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

    // Listar usuários por plano
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

    // --------------- MÉTODOS UTILITÁRIOS ---------------

    // Valida se o limite de aluguéis do usuário foi excedido
    void validateUserRentalLimit(User user) {

        int maxRentals = switch (user.getPlan()) {
            case NOOB -> 1;
            case PRO -> 3;
            case LEGEND -> 5;
        };
        if (user.getActiveRentals() >= maxRentals) {
            throw new PlanLimitExceededException(user);
        }
    }

    // Atualiza contagem de alugués ativos do usuário
    void updateUserActiveRentalsCount(User user, int x) {

        user.setActiveRentals(user.getActiveRentals() + x);
        userRepository.save(user);
    }
}
