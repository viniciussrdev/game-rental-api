package dev.viniciussr.gamerental.repository;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import dev.viniciussr.gamerental.enums.UserRole;
import dev.viniciussr.gamerental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Repositório responsável pelas operações de acesso a dados da entidade User
// Estende JpaRepository para fornecer funcionalidades básicas de CRUD
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNameContainingIgnoreCase(String name);

    List<User> findByEmailContainingIgnoreCase(String email);

    List<User> findByPlan(SubscriptionPlans plan);

    List<User> findByRole(UserRole role);

    Optional<User> findByEmail(String email);
}