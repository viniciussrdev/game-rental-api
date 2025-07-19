package dev.viniciussr.gamerental.service;

import dev.viniciussr.gamerental.dto.GameDto;
import dev.viniciussr.gamerental.enums.GameGenres;
import dev.viniciussr.gamerental.exception.game.*;
import dev.viniciussr.gamerental.model.Game;
import dev.viniciussr.gamerental.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // -------------------- CRUD BÁSICO --------------------

    // Cria um novo jogo
    public GameDto createGame(GameDto dto) {
        
        Game savedGame = new Game(
                dto.title(),
                dto.genre(),
                dto.platform(),
                dto.quantity(),
                true
        );
        return new GameDto(gameRepository.save(savedGame));
    }

    // Atualiza um jogo existente
    public GameDto updateGame(Long id, GameDto dto) {

        Game updatedGame = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Jogo não encontrado no id: " + id));

        updatedGame.setTitle(dto.title());
        updatedGame.setGenre(dto.genre());
        updatedGame.setPlatform(dto.platform());
        updatedGame.setQuantity(dto.quantity());
        updatedGame.setAvailable(dto.available());

        return new GameDto(gameRepository.save(updatedGame));
    }

    // Remove um jogo existente
    public void deleteGame(Long id) {

        Game deletedGame = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Jogo não encontrado no id: " + id));

        gameRepository.delete(deletedGame);
    }

    // --------------- FILTROS ---------------

    // Busca um jogo pelo ID
    public GameDto findGameById(Long id) {

        return gameRepository.findById(id)
                .map(GameDto::new)
                .orElseThrow(() -> new GameNotFoundException("Jogo não encontrado no id: " + id));
    }

    // Lista todos os jogos cadastrados
    public List<GameDto> listGames() {

        List<GameDto> games = gameRepository.findAll()
                .stream()
                .map(GameDto::new)
                .toList();

        if (games.isEmpty()) {
            throw new GameNotFoundException("Nenhum jogo cadastrado no momento");
        }
        return games;
    }

    // Lista jogos pelo título
    public List<GameDto> listGamesByTitle(String title) {

        List<GameDto> games = gameRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(GameDto::new)
                .toList();

        if (games.isEmpty()) {
            throw new GameNotFoundException("Nenhum jogo encontrado com o título: " + title);
        }
        return games;
    }

    // Lista jogos pelo gênero
    public List<GameDto> listGamesByGenre(GameGenres genre) {

        List<GameDto> games = gameRepository.findByGenre(genre)
                .stream()
                .map(GameDto::new)
                .toList();

        if (games.isEmpty()) {
            throw new GameNotFoundException("Nenhum jogo encontrado do gênero: " + genre);
        }
        return games;
    }

    // Lista todos os jogos disponíveis para aluguel
    public List<GameDto> listAvailableGames() {

        List<GameDto> games = gameRepository.findByAvailableTrue()
                .stream()
                .map(GameDto::new)
                .toList();

        if (games.isEmpty()) {
            throw new GameNotFoundException("Nenhum jogo disponível no momento");
        }
        return games;
    }

    // --------------- MÉTODOS UTILITÁRIOS ---------------

    // Valida se o jogo está disponível para aluguel
    void validateIfGameIsAvailable(Game game) {

        if (game.getQuantity() <= 0) {
            throw new GameIsNotAvailableException(game);
        }
    }

    // Atualiza a quantidade e a disponibilidade do jogo após aluguel ou devolução
    void updateGameQuantityAndAvailability(Game game, int x) {

        game.setQuantity(game.getQuantity() + x);
        game.setAvailable(game.getQuantity() > 0);
        gameRepository.save(game);
    }
}

