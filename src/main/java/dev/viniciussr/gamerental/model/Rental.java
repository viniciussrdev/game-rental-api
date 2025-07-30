package dev.viniciussr.gamerental.model;

import dev.viniciussr.gamerental.enums.RentalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
    private Long idRental; // ID do aluguel

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game; // Jogo incluído no aluguel

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Usuário solicitante do aluguel

    private LocalDate rentalDate; // Data do aluguel

    private LocalDate endDate; // Data de devolução do aluguel

    @Enumerated(EnumType.STRING)
    private RentalStatus status; // Status do aluguel

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