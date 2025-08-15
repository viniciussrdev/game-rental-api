package dev.viniciussr.gamerental.controller;

import dev.viniciussr.gamerental.dto.GameDto;
import dev.viniciussr.gamerental.dto.GameUpdateDto;
import dev.viniciussr.gamerental.enums.GameGenres;
import dev.viniciussr.gamerental.exception.game.GameNotFoundException;
import dev.viniciussr.gamerental.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por gerenciar as operações relacionadas a jogos.
 * <p>
 * Disponibiliza endpoints para criação, atualização, exclusão, busca e listagem de jogos,
 * com filtros adicionais por título, gênero e disponibilidade para aluguel.
 * </p>
 */
@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Endpoint para criar um novo jogo no sistema.
     *
     * @param dto objeto {@link GameDto} contendo os dados do jogo a ser criado.
     * @return {@link ResponseEntity} com o jogo criado e status {@code 201 Created}.
     */
    @PostMapping
    public ResponseEntity<GameDto> createGame(@RequestBody @Valid GameDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createGame(dto));
    }

    /**
     * Endpoint para atualizar os dados de um jogo existente.
     *
     * @param id  identificador do jogo a ser atualizado.
     * @param dto objeto {@link GameUpdateDto} contendo os novos dados do jogo.
     * @return {@link ResponseEntity} com o {@link GameDto} do jogo atualizado.
     * @throws GameNotFoundException se o jogo não for encontrado.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<GameDto> updateGame(@PathVariable Long id, @RequestBody @Valid GameUpdateDto dto) {
        return ResponseEntity.ok(gameService.updateGame(id, dto));
    }

    /**
     * Endpoint para remover um jogo do sistema.
     *
     * @param id identificador do jogo a ser removido.
     * @return {@link ResponseEntity} com status {@code 204 No Content}.
     * @throws GameNotFoundException se o jogo não for encontrado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para buscar um jogo pelo seu identificador.
     *
     * @param id identificador do jogo.
     * @return {@link ResponseEntity} contendo o {@link GameDto} do jogo encontrado.
     * @throws GameNotFoundException se o jogo não for encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameDto> findGameById(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.findGameById(id));
    }

    /**
     * Endpoint para listar todos os jogos cadastrados no sistema.
     *
     * @return {@link ResponseEntity} contendo uma lista de {@link GameDto}.
     * @throws GameNotFoundException se não houver jogos cadastrados.
     */
    @GetMapping
    public ResponseEntity<List<GameDto>> listGames() {
        return ResponseEntity.ok(gameService.listGames());
    }

    /**
     * Endpoint para listar jogos filtrados por título.
     *
     * @param title título do jogo ou parte dele.
     * @return {@link ResponseEntity} contendo uma lista de {@link GameDto} que correspondem ao título informado.
     * @throws GameNotFoundException se nenhum jogo não for encontrado.
     */
    @GetMapping(params = "title")
    public ResponseEntity<List<GameDto>> listGamesByTitle(@RequestParam String title) {
        return ResponseEntity.ok(gameService.listGamesByTitle(title));
    }

    /**
     * Endpoint para listar jogos filtrados por gênero.
     *
     * @param genre gênero do jogo, conforme o enum {@link GameGenres}.
     * @return {@link ResponseEntity} contendo uma lista de {@link GameDto} do gênero informado.
     * @throws GameNotFoundException se nenhum jogo não for encontrado.
     */
    @GetMapping(params = "genre")
    public ResponseEntity<List<GameDto>> listGamesByGenre(@RequestParam GameGenres genre) {
        return ResponseEntity.ok(gameService.listGamesByGenre(genre));
    }

    /**
     * Endpoint para listar todos os jogos disponíveis para aluguel.
     *
     * @return {@link ResponseEntity} contendo uma lista de {@link GameDto} disponíveis para locação.
     * @throws GameNotFoundException se nenhum jogo não for encontrado.
     */
    @GetMapping("/available")
    public ResponseEntity<List<GameDto>> listAvailableGames() {
        return ResponseEntity.ok(gameService.listAvailableGames());
    }
}