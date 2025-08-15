package dev.viniciussr.gamerental.model;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import dev.viniciussr.gamerental.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;

/** Entidade que representa um usuário cadastrado na loja.
 * <p>
 * Implementa a interface {@link UserDetailsService} do Spring Security,
 * usada para controle de autenticação e autorização.
 */
@Entity
@Table(name = "tb_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    /** identificador único do usuário. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    /** Nome completo do usuário. */
    private String name;

    /** Endereço de e-mail do usuário (login). */
    private String email;

    /** Senha do usuário (login). */
    private String password;

    /** Função (role) do usuário (ADMIN ou USER). */
    @Enumerated(EnumType.STRING)
    private UserRole role;

    /** Plano de assinaturas do usuário (NOOB, PRO ou LEGEND). */
    @Enumerated(EnumType.STRING)
    private SubscriptionPlans plan;

    /** Quantidade de aluguéis ativos do usuário. */
    private Integer activeRentals;

    /**
     * Construtor para criação de um novo usuário.
     *
     * @param name          nome do usuário.
     * @param email         e-mail do usuário.
     * @param password      senha do usuário.
     * @param role          função do usuário.
     * @param plan          plano de assinatura.
     * @param activeRentals quantidade de aluguéis ativos.
     */
    public User(
            String name,
            String email,
            String password,
            UserRole role,
            SubscriptionPlans plan,
            Integer activeRentals
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.plan = plan;
        this.activeRentals = activeRentals;
    }

    /**
     * Retorna as permissões (authorities) do usuário de acordo com sua role.
     * <p>
     * ADMIN recebe permissões de ADMIN e USER.
     * USER recebe somente permissões de USER.
     *
     * @return lista de permissões do usuário
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (role == null) return List.of(); // Garante que o app não quebre se receber role 'null'

        return switch (role) {
            case ADMIN -> List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
            case USER -> List.of(
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        };
    }

    /**
     * Retorna o e-mail como identificador (username) do usuário no sistema.
     *
     * @return e-mail do usuário
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Verifica se a conta está expirada.
     * {@inheritDoc}
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * Verifica se a conta está bloqueada.
     * {@inheritDoc}
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * Verifica se as credenciais estão expiradas.
     * {@inheritDoc}
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * Verifica se o usuário está habilitado.
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}