package dev.viniciussr.gamerental.model;

import dev.viniciussr.gamerental.enums.GameGenres;
import dev.viniciussr.gamerental.enums.Platforms;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Entidade que representa um jogo disponível na loja.
 */
@Entity
@Table(name = "tb_game")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    /** Identificador único do jogo. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_game")
    private Long idGame;

    /** Título do jogo. */
    private String title;

    /** Gênero do jogo. */
    @Enumerated(EnumType.STRING)
    private GameGenres genre;

    /** Plataformas em que o jogo está disponível na loja. */
    @ElementCollection(targetClass = Platforms.class)
    @CollectionTable(
            name = "tb_game_platform",
            joinColumns = @JoinColumn(name = "game_id"))
    @Enumerated(EnumType.STRING)
    private Set<Platforms> platform;

    /** Quantidade de cópias do jogo disponíveis na loja. */
    private Integer quantity;

    /** Status de disponibilidade do jogo (true or false). */
    private boolean available;

    /**
     * Construtor para criação de um novo jogo.
     *
     * @param title     título do jogo.
     * @param genre     gênero do jogo.
     * @param platform  plataformas onde o jogo está disponível.
     * @param quantity  quantidade de cópias disponíveis.
     * @param available status de disponibilidade do jogo.
     */
    public Game(
            String title,
            GameGenres genre,
            Set<Platforms> platform,
            Integer quantity,
            boolean available
    ) {
        this.title = title;
        this.genre = genre;
        this.platform = platform;
        this.quantity = quantity;
        this.available = available;
    }
}