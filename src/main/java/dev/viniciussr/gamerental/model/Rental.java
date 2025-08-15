package dev.viniciussr.gamerental.model;

import dev.viniciussr.gamerental.enums.RentalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/** Entidade que representa um aluguel efetuado na loja.
 * <p>
 * Cada aluguel está relacionado a um único jogo e usuário.
 */
@Entity
@Table(name = "tb_rental")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rental {

    /** Identificador único do aluguel. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rental")
    private Long idRental;

    /** Referência ao jogo alugado. */
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    /** Referência ao usuário solicitante do aluguel. */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /** Data de início do aluguel. */
    private LocalDate rentalDate;

    /** Data de encerramento prevista do aluguel. */
    private LocalDate endDate;

    /** Status atual do aluguel (ACTIVE, RETURNED, LATE ou CANCELLED). */
    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    /**
     * Construtor para criação de um novo aluguel.
     *
     * @param game       referência ao jogo alugado.
     * @param user       referência ao usuário solicitante.
     * @param rentalDate data de início do aluguel.
     * @param endDate    data prevista para encerramento.
     * @param status     status atual do aluguel.
     */
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