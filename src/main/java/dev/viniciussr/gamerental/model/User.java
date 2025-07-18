package dev.viniciussr.gamerental.model;

import dev.viniciussr.gamerental.enums.SubscriptionPlans;
import jakarta.persistence.*;
import lombok.*;

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

    @Enumerated(EnumType.STRING)
    private SubscriptionPlans plan; // Plano de assinatura do usuário

    private Integer activeRentals; // Aluguéis ativos do usuário

    public User(
            String name,
            String email,
            SubscriptionPlans plan,
            Integer activeRentals
    ) {
        this.name = name;
        this.email = email;
        this.plan = plan;
        this.activeRentals = activeRentals;
    }
}