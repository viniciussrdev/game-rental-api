package dev.viniciussr.gamerental.model;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import dev.viniciussr.gamerental.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser; // ID do usuário

    private String name; // Nome do usuário

    private String email; // Email do usuário

    private String password; // Senha do usuário

    @Enumerated(EnumType.STRING)
    private UserRole role; // Tipo de usuário

    @Enumerated(EnumType.STRING)
    private SubscriptionPlans plan; // Plano de assinatura do usuário

    private Integer activeRentals; // Aluguéis ativos do usuário

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

    // --------------- MÉTODOS DA INTERFACE USERDETAILS (Spring Security) ---------------

    // Retorna as permissões (authorities) do usuário de acordo com sua role
    // ADMIN recebe permissões de ADMIN e USER. USER recebe permissões só de USER.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (role == null) return List.of(); // Garante que o app não quebre se role for 'null'

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

    // Retorna o email como identificador do usuário no sistema (username)
    @Override
    public String getUsername() {
        return this.email;
    }

    // Indica se a conta está expirada
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    // Indica se a conta está bloqueada
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    // Indica se as credenciais estão expiradas
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    // Indica se o usuário está habilitado
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}