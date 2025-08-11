package dev.viniciussr.gamerental.model;

import dev.viniciussr.gamerental.enums.RentalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// Entidade que representa um aluguel efetuado na loja
// Cada aluguel está relacionado a um único jogo e um único usuário
@Entity
@Table(name = "tb_rental")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rental")
    private Long idRental; // Identificador único do aluguel

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game; // Referência ao jogo incluído no aluguel

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Referência ao usuário solicitante do aluguel

    private LocalDate rentalDate; // Data de início do aluguel

    private LocalDate endDate; // Data prevista de devolução do aluguel

    @Enumerated(EnumType.STRING)
    private RentalStatus status; // Status atual do aluguel (ACTIVE, RETURNED, LATE ou CANCELLED)

    public Rental(
            Game game,
            User user,
            LocalDate rentalDate,
            LocalDate endDate,
            RentalStatus status
    ) {
        this.game = game;
        this.user = user;
        this.rentalDate = rentalDate;
        this.endDate = endDate;
        this.status = status;
    }
}