package dev.viniciussr.gamerental.repository;

import dev.viniciussr.gamerental.enums.GameGenres;
import dev.viniciussr.gamerental.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repositório responsável pelas operações de acesso a dados da entidade Game
// Estende JpaRepository para fornecer funcionalidades básicas de CRUD
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByTitleContainingIgnoreCase(String title);

    List<Game> findByGenre(GameGenres genre);

    List<Game> findByAvailableTrue();
}