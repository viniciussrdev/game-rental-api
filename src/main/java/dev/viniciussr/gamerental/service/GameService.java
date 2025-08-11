package dev.viniciussr.gamerental.service;

import dev.viniciussr.gamerental.dto.GameDto;
import dev.viniciussr.gamerental.dto.GameUpdateDto;
import dev.viniciussr.gamerental.enums.GameGenres;
import dev.viniciussr.gamerental.exception.game.GameIsNotAvailableException;
import dev.viniciussr.gamerental.exception.game.GameNotFoundException;
import dev.viniciussr.gamerental.model.Game;
import dev.viniciussr.gamerental.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Serviço responsável por gerenciar operações relacionadas aos jogos da aplicação
@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // ******************************
    // CRUD BÁSICO
    // ******************************

    // Cria um novo jogo
    public GameDto createGame(GameDto dto) {
        
        Game savedGame = new Game(
                dto.title(),
                dto.genre(),
                dto.platform(),
                dto.quantity(),
                true // Jogo é criado como 'disponivel'
        );
        return new GameDto(gameRepository.save(savedGame));
    }

    // Atualiza um jogo existente
    public GameDto updateGame(Long id, GameUpdateDto dto) {

        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Jogo não encontrado no id: " + id));

        // Atualização parcial (PATCH)
        // Se algum atributo chegar nulo, não ocorrerá a atualização do campo
        if (dto.title()    != null) game.setTitle(dto.title());
        if (dto.genre()    != null) game.setGenre(dto.genre());
        if (dto.platform() != null) game.setPlatform(dto.platform());
        if (dto.quantity() != null) game.setQuantity(dto.quantity());

        return new GameDto(gameRepository.save(game));
    }

    // Deleta um jogo existente
    public void deleteGame(Long id) {

        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Jogo não encontrado no id: " + id));

        gameRepository.delete(game);
    }

    // ******************************
    // MÉTODOS DE BUSCA
    // ******************************

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

        List<GameDto> games = gameRepository
                .findByTitleContainingIgnoreCase(title) // Busca ocorrências parciais e ignora maiúsculas/minúsculas
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

    // ******************************
    // LÓGICA DE NEGÓCIO
    // ******************************

    // Valida se o jogo está disponível para aluguel
    void validateIfGameIsAvailable(Game game) {

        if (game.getQuantity() <= 0) { // Verifica se quantidade do jogo é menor ou igual a zero
            throw new GameIsNotAvailableException(game);
        }
    }

    // Atualiza a quantidade e a disponibilidade do jogo após novo aluguel ou devolução
    // Utilizado nos métodos 'createRental', 'returnRental' e 'cancelRental'
    void updateGameQuantityAndAvailability(Game game, int x) {

        // Novo aluguel (x = -1) → quantidade DIMINUI 1; Devolução (x = 1) → quantidade AUMENTA 1
        game.setQuantity(game.getQuantity() + x);

        // Quantidade final do jogo maior que zero → jogo disponível
        game.setAvailable(game.getQuantity() > 0);

        gameRepository.save(game);
    }
}