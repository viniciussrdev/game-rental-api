package dev.viniciussr.gamerental.model;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import dev.viniciussr.gamerental.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

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
}