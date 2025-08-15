package dev.viniciussr.gamerental.repository;

import dev.viniciussr.gamerental.enums.GameGenres;
import dev.viniciussr.gamerental.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório responsável pelas operações de acesso a dados da entidade {@link Game}.
 * <p>
 * Estende {@link JpaRepository} para fornecer funcionalidades básicas de CRUD.
 * </p>
 */
public interface GameRepository extends JpaRepository<Game, Long> {

    /**
     * Lista jogos cujo título contenha o texto fornecido,
     * ignorando diferenças entre maiúsculas e minúsculas.
     *
     * @param title String a ser buscada no título do jogo.
     * @return Lista de jogos filtrada pelo título informado.
     */
    List<Game> findByTitleContainingIgnoreCase(String title);

    /**
     * Lista jogos pelo gênero fornecido.
     *
     * @param genre gênero do jogo a ser buscado.
     * @return Lista de jogos filtrada pelo gênero informado.
     */
    List<Game> findByGenre(GameGenres genre);

    /**
     * Lista todos os jogos que estão disponíveis.
     *
     * @return Lista de jogos com disponibilidade igual a true.
     */
    List<Game> findByAvailableTrue();
}