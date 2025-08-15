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

/**
 * Serviço responsável por gerenciar operações relacionadas aos usuários da aplicação.
 * <p>
 * Inclui criação, atualização, exclusão, busca e regras de negócio.
 * </p>
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo usuário no sistema.
     * Usuário é criado por padrão com senha criptografada, role 'USER' e sem aluguéis ativos.
     *
     * @param dto objeto com os dados do usuário a ser criado.
     * @return DTO do usuário criado ({@link UserDto}).
     */
    public UserDto createUser(UserRegisterDto dto) {

        // Criptografa a senha
        String encryptedPassword = passwordEncoder.encode(dto.password());

        User user = new User(
                dto.name(),
                dto.email(),
                encryptedPassword, // Senha criptografada
                UserRole.USER, // Role padrão 'USER'
                dto.plan(),
                0 // Zero aluguéis ativos
        );
        return new UserDto(userRepository.save(user));
    }

    /**
     * Atualiza os dados de um usuário existente.
     * <p>
     * Apenas os campos não nulos no DTO serão atualizados.
     * </p>
     *
     * @param id  ID do usuário a ser atualizado.
     * @param dto DTO com os dados de atualização.
     * @return DTO do usuário atualizado ({@link UserDto}).
     * @throws UserNotFoundException se o usuário não for encontrado.
     */
    public UserDto updateUser(Long id, UserUpdateDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado no id: " + id));

        if (dto.name()     != null) user.setName(dto.name());
        if (dto.email()    != null) user.setEmail(dto.email());
        if (dto.password() != null) user.setPassword(passwordEncoder.encode(dto.password()));
        if (dto.role()     != null) user.setRole(dto.role());
        if (dto.plan()     != null) user.setPlan(dto.plan());

        return new UserDto(userRepository.save(user));
    }

    /**
     * Remove um usuário existente do sistema.
     *
     * @param id ID do usuário a ser deletado.
     * @throws UserNotFoundException se o usuário não for encontrado.
     */
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado no id: " + id));

        userRepository.delete(user);
    }

    // ******************************
    // MÉTODOS DE BUSCA
    // ******************************

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id ID do usuário.
     * @return DTO do usuário encontrado pelo ID ({@link UserDto}).
     * @throws UserNotFoundException se o usuário não for encontrado.
     */
    public UserDto findUserById(Long id) {

        return userRepository.findById(id)
                .map(UserDto::new)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado no id: " + id));

    }

    /**
     * Lista todos os usuários cadastrados.
     *
     * @return Lista de todos os usuários {@link UserDto}.
     * @throws UserNotFoundException se não houver usuários cadastrados.
     */
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

    /**
     * Lista usuários cujo nome contenha a String informada
     * (ignora maiúsculas/minúsculas).
     *
     * @param name nome parcial ou completo do usuário.
     * @return Lista de usuários com o nome informado ({@link UserDto}).
     * @throws UserNotFoundException se nenhum usuário for encontrado.
     */
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

    /**
     * Lista todos os usuários pelo endereço de e-mail.
     *
     * @param email endereço de e-mail do usuário.
     * @return Lista de usuários pelo e-mail informado ({@link UserDto}).
     * @throws UserNotFoundException se nenhum usuário for encontrado.
     */
    public List<UserDto> listUsersByEmail(String email) {

        List<UserDto> users = userRepository
                .findByEmailContainingIgnoreCase(email)
                .stream()
                .map(UserDto::new)
                .toList();
        if (users.isEmpty()) {
            throw new UserNotFoundException("Nenhum Usuário encontrado com o Email: " + email);
        }
        return users;
    }

    /**
     * Lista todos os usuários pela role fornecida.
     *
     * @param role função do usuário.
     * @return Lista de usuários pela role informado ({@link UserDto}).
     * @throws UserNotFoundException se nenhum usuário for encontrado.
     */
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

    /**
     * Lista todos os usuários pelo plano fornecido.
     *
     * @param plan plano de assinatura do usuário.
     * @return Lista de usuários pelo plano informado ({@link UserDto}).
     * @throws UserNotFoundException se nenhum usuário for encontrado.
     */
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

    /**
     * Valida se o usuário atingiu o limite de aluguéis ativos permitido pelo seu plano.
     *
     * @param user usuário a ser verificado.
     * @throws PlanLimitExceededException se o limite do plano for excedido.
     */
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

    /**
     * Atualiza o contador de aluguéis ativos do usuário.
     * <p>
     * Utilizado tanto para incrementar quanto para decrementar o número de aluguéis ativos.
     * </p>
     *
     * @param user usuário a ser atualizado.
     * @param x    Valor a ser adicionado (1 para novo aluguel, -1 para devolução)
     */
    void updateUserActiveRentalsCount(User user, int x) {

        // Novo aluguel (x = 1); Devolução (x = -1)
        user.setActiveRentals(user.getActiveRentals() + x);

        userRepository.save(user);
    }

    // ******************************
    // USER DETAILS SERVICE
    // ******************************

    /**
     * Busca um usuário pelo email para autenticação no Spring Security.
     *
     * @param email endereço de e-mail do usuário.
     * @return Usuário encontrado.
     * @throws UsernameNotFoundException se o usuário não for encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}