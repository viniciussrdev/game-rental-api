package dev.viniciussr.gamerental.repository;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import dev.viniciussr.gamerental.enums.UserRole;
import dev.viniciussr.gamerental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelas operações de acesso a dados da entidade {@link User}.
 * <p>
 * Estende {@link JpaRepository} para fornecer funcionalidades básicas de CRUD.
 * </p>
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Lista usuários cujo nome contenha a string fornecida,
     * ignorando diferenças entre maiúsculas e minúsculas.
     *
     * @param name String a ser buscada no nome do usuário.
     * @return Lista de usuários filtrada pelo nome informado
     */
    List<User> findByNameContainingIgnoreCase(String name);

    /**
     * Lista usuários cujo e-mail contenha a string fornecida,
     * ignorando diferenças entre maiúsculas e minúsculas.
     *
     * @param email String a ser buscada no e-mail do usuário.
     * @return Lista de usuários filtrada pelo e-mail informado.
     */
    List<User> findByEmailContainingIgnoreCase(String email);

    /**
     * Lista usuários pelo plano de assinatura fornecido.
     *
     * @param plan plano de Assinatura a ser buscado.
     * @return Lista de usuários filtrada pelo plano informado.
     */
    List<User> findByPlan(SubscriptionPlans plan);

    /**
     * Lista usuários pela role fornecida.
     *
     * @param role função (role) a ser buscada.
     * @return Lista de usuários filtrada pela função informada.
     */
    List<User> findByRole(UserRole role);

    /**
     * Busca um usuário pelo email fornecido.
     *
     * @param email e-mail do usuário a ser buscado.
     * @return {@link Optional} contendo o usuário, caso encontrado.
     */
    Optional<User> findByEmail(String email);
}