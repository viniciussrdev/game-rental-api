package dev.viniciussr.gamerental.controller;

import dev.viniciussr.gamerental.dto.GameDto;
import dev.viniciussr.gamerental.dto.GameUpdateDto;
import dev.viniciussr.gamerental.enums.GameGenres;
import dev.viniciussr.gamerental.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games") // Rota base para as requisições deste controller
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // Endpoint para criar/adicionar novo jogo
    @PostMapping
    public ResponseEntity<GameDto> createGame(@RequestBody @Valid GameDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createGame(dto));
    }

    // Endpoint para atualizar jogo existente
    @PatchMapping("/{id}")
    public ResponseEntity<GameDto> updateGame(@PathVariable Long id, @RequestBody @Valid GameUpdateDto dto) {
        return ResponseEntity.ok(gameService.updateGame(id, dto));
    }

    // Endpoint para deletar jogo do banco de dados
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para buscar jogo por ID
    @GetMapping("/{id}")
    public ResponseEntity<GameDto> findGameById(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.findGameById(id));
    }

    // Endpoint para listar todos os jogos
    @GetMapping
    public ResponseEntity<List<GameDto>> listGames() {
        return ResponseEntity.ok(gameService.listGames());
    }

    // Endpoint para listar jogos por título
    @GetMapping(params = "title")
    public ResponseEntity<List<GameDto>> listGamesByTitle(@RequestParam String title) {
        return ResponseEntity.ok(gameService.listGamesByTitle(title));
    }

    // Endpoint para listar jogos por gênero
    @GetMapping(params = "genre")
    public ResponseEntity<List<GameDto>> listGamesByGenre(@RequestParam GameGenres genre) {
        return ResponseEntity.ok(gameService.listGamesByGenre(genre));
    }

    // Endpoint para listar todos os jogos disponíveis para aluguel
    @GetMapping("/available")
    public ResponseEntity<List<GameDto>> listAvailableGames() {
        return ResponseEntity.ok(gameService.listAvailableGames());
    }
}