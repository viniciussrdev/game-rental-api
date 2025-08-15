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

/**
 * Serviço responsável por gerenciar operações relacionadas aos jogos da aplicação.
 * <p>
 * Inclui criação, atualização, exclusão, busca e regras de negócio.
 * </p>
 */
@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // ******************************
    // CRUD BÁSICO
    // ******************************

    /**
     * Cria um novo jogo no sistema.
     * <p>
     * Jogo é criado por padrão com disponibilidade: true.
     * </p>
     *
     * @param dto objeto com os dados do jogo a ser criado.
     * @return DTO do jogo criado ({@link GameDto}).
     */
    public GameDto createGame(GameDto dto) {
        
        Game savedGame = new Game(
                dto.title(),
                dto.genre(),
                dto.platform(),
                dto.quantity(),
                true // Disponível
        );
        return new GameDto(gameRepository.save(savedGame));
    }

    /**
     * Atualiza os dados de um jogo existente.
     * <p>
     * Apenas os campos não nulos no DTO serão atualizados.
     * </p>
     *
     * @param id  ID do jogo a ser atualizado.
     * @param dto DTO com os dados de atualização.
     * @return DTO do jogo atualizado ({@link GameDto}).
     * @throws GameNotFoundException se o jogo não for encontrado.
     */
    public GameDto updateGame(Long id, GameUpdateDto dto) {

        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Jogo não encontrado no id: " + id));

        if (dto.title()    != null) game.setTitle(dto.title());
        if (dto.genre()    != null) game.setGenre(dto.genre());
        if (dto.platform() != null) game.setPlatform(dto.platform());
        if (dto.quantity() != null) game.setQuantity(dto.quantity());

        return new GameDto(gameRepository.save(game));
    }

    /**
     * Remove um jogo existente do sistema.
     *
     * @param id ID do jogo a ser deletado.
     * @throws GameNotFoundException se o jogo não for encontrado.
     */
    public void deleteGame(Long id) {

        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Jogo não encontrado no id: " + id));

        gameRepository.delete(game);
    }

    // ******************************
    // MÉTODOS DE BUSCA
    // ******************************

    /**
     * Busca um jogo pelo seu ID.
     *
     * @param id ID do jogo.
     * @return DTO do jogo encontrado pelo ID ({@link GameDto}).
     * @throws GameNotFoundException caso o jogo não seja encontrado.
     */
    public GameDto findGameById(Long id) {

        return gameRepository.findById(id)
                .map(GameDto::new)
                .orElseThrow(() -> new GameNotFoundException("Jogo não encontrado no id: " + id));
    }

    /**
     * Lista todos os jogos cadastrados.
     *
     * @return Lista de todos os jogos ({@link GameDto}).
     * @throws GameNotFoundException se não houver jogos cadastrados.
     */
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

    /**
     * Lista jogos cujo título contenha a String informada
     * (ignora maiúsculas/minúsculas).
     *
     * @param title título parcial ou completo do jogo.
     * @return Lista de jogos com o título informado ({@link GameDto}).
     * @throws GameNotFoundException se nenhum jogo for encontrado.
     */
    public List<GameDto> listGamesByTitle(String title) {

        List<GameDto> games = gameRepository
                .findByTitleContainingIgnoreCase(title)
                .stream()
                .map(GameDto::new)
                .toList();

        if (games.isEmpty()) {
            throw new GameNotFoundException("Nenhum jogo encontrado com o título: " + title);
        }
        return games;
    }

    /**
     * Lista todos os jogos de um determinado gênero.
     *
     * @param genre gênero do jogo.
     * @return Lista de jogos do gênero informado ({@link GameDto}).
     * @throws GameNotFoundException se nenhum jogo for encontrado.
     */
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

    /**
     * Lista todos os jogos disponíveis para aluguel.
     *
     * @return Lista de todos os jogos disponíveis ({@link GameDto}).
     * @throws GameNotFoundException se não houver jogos disponíveis.
     */
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

    /**
     * Valida se o jogo está disponível para aluguel.
     *
     * @param game jogo a ser verificado.
     * @throws GameIsNotAvailableException se a quantidade do jogo for menor ou igual a zero.
     */
    void validateIfGameIsAvailable(Game game) {

        if (game.getQuantity() <= 0) {
            throw new GameIsNotAvailableException(game);
        }
    }

    /**
     * Atualiza a quantidade e a disponibilidade de um jogo.
     * <p>
     * Utilizado nos métodos {@code createRental}, {@code returnRental} e {@code cancelRental}.
     * </p>
     *
     * @param game jogo a ser atualizado.
     * @param x    quantidade a ser somada ou subtraída:
     *             {@code -1} para novo aluguel (reduz quantidade),
     *             {@code 1} para devolução ou cancelamento (aumenta quantidade).
     */
    void updateGameQuantityAndAvailability(Game game, int x) {

        // Aumenta/reduz a quantidade do jogo
        game.setQuantity(game.getQuantity() + x);

        // Atualiza disponibilidade
        game.setAvailable(game.getQuantity() > 0);

        gameRepository.save(game);
    }
}