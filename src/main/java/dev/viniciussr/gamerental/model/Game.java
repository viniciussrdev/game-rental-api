package dev.viniciussr.gamerental.model;

import dev.viniciussr.gamerental.enums.GameGenres;
import dev.viniciussr.gamerental.enums.Platforms;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "tb_game")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_game")
    private Long idGame; // ID do jogo

    private String title; // Título do jogo

    @Enumerated(EnumType.STRING)
    private GameGenres genre; // Gênero do jogo

    @ElementCollection(targetClass = Platforms.class)
    @CollectionTable(
            name = "tb_game_platform",
            joinColumns = @JoinColumn(name = "game_id"))
    @Enumerated(EnumType.STRING)
    private Set<Platforms> platform; // Plataformas onde o jogo está disponível na loja

    private Integer quantity; // Quantidade disponível do jogo na loja

    private boolean available; // Disponibilidade do jogo (true or false)

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