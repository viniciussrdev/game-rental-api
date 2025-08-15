package dev.viniciussr.gamerental.controller;

import dev.viniciussr.gamerental.dto.RentalDto;
import dev.viniciussr.gamerental.dto.RentalUpdateDto;
import dev.viniciussr.gamerental.enums.RentalStatus;
import dev.viniciussr.gamerental.exception.game.GameNotFoundException;
import dev.viniciussr.gamerental.exception.rental.RentalAlreadyClosedException;
import dev.viniciussr.gamerental.exception.rental.RentalNotFoundException;
import dev.viniciussr.gamerental.exception.user.UserNotFoundException;
import dev.viniciussr.gamerental.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador responsável por gerenciar as operações relacionadas a aluguéis de jogos.
 * <p>
 * Disponibiliza endpoints para criação, atualização, exclusão, devolução, renovação e cancelamento de aluguéis,
 * bem como busca e listagem de aluguéis com filtros adicionais por jogo, usuário, datas e status.
 * </p>
 */
@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    /**
     * Endpoint para criar um novo aluguel no sistema.
     *
     * @param dto objeto {@link RentalDto} contendo os dados do aluguel a ser criado.
     * @return {@link ResponseEntity} com o aluguel criado e status {@code 201 Created}.
     */
    @PostMapping
    public ResponseEntity<RentalDto> createRental(@RequestBody @Valid RentalDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalService.createRental(dto));
    }

    /**
     * Endpoint para atualizar as informações de um aluguel existente.
     *
     * @param id  identificador do aluguel a ser atualizado.
     * @param dto objeto {@link RentalUpdateDto} contendo os novos dados do aluguel.
     * @return {@link ResponseEntity} com o {@link RentalDto} do aluguel atualizado
     * @throws RentalNotFoundException      se o aluguel não for encontrado.
     * @throws RentalAlreadyClosedException se o aluguel já estiver encerrado.
     * @throws GameNotFoundException        se o novo jogo (quando informado) não for encontrado.
     * @throws UserNotFoundException        se o novo usuário (quando informado) não for encontrado.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<RentalDto> updateRental(@PathVariable Long id, @RequestBody @Valid RentalUpdateDto dto) {
        return ResponseEntity.ok(rentalService.updateRental(id, dto));
    }

    /**
     * Endpoint para remover um aluguel do sistema.
     *
     * @param id identificador do aluguel.
     * @return {@link ResponseEntity} com status {@code 204 No Content}.
     * @throws RentalNotFoundException      se o aluguel não for encontrado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
        rentalService.deleteRental(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para registrar a devolução de um aluguel ativo.
     *
     * @param id identificador do aluguel.
     * @return {@link ResponseEntity} com status {@code 204 No Content}.
     * @throws RentalNotFoundException se o aluguel não for encontrado.
     */
    @PutMapping("/return/{id}")
    public ResponseEntity<Void> returnRental(@PathVariable Long id) {
        rentalService.returnRental(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para renovar um aluguel por mais 7 dias.
     *
     * @param id identificador do aluguel.
     * @return {@link ResponseEntity} com status {@code 204 No Content}.
     * @throws RentalNotFoundException se o aluguel não for encontrado.
     */
    @PutMapping("/renew/{id}")
    public ResponseEntity<Void> renewRental(@PathVariable Long id) {
        rentalService.renewRental(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para cancelar um aluguel.
     *
     * @param id identificador do aluguel.
     * @return {@link ResponseEntity} com status {@code 204 No Content}.
     * @throws RentalNotFoundException se o aluguel não for encontrado.
     */
    @PutMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelRental(@PathVariable Long id) {
        rentalService.cancelRental(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para buscar um aluguel pelo seu identificador.
     *
     * @param id identificador do aluguel.
     * @return {@link ResponseEntity} contendo o {@link RentalDto} do aluguel encontrado.
     * @throws RentalNotFoundException se o aluguel não for encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RentalDto> findRentalById(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.findRentalById(id));
    }

    /**
     * Endpoint para listar todos os aluguéis cadastrados no sistema.
     *
     * @return {@link ResponseEntity} contendo uma lista de {@link RentalDto}.
     * @throws RentalNotFoundException se não houver aluguéis cadastrados.
     */
    @GetMapping
    public ResponseEntity<List<RentalDto>> listRentals() {
        return ResponseEntity.ok(rentalService.listRentals());
    }

    /**
     * Endpoint para listar aluguéis filtrados pelo ID do jogo.
     *
     * @param gameId identificador do jogo.
     * @return {@link ResponseEntity} contendo uma lista de {@link RentalDto}.
     * @throws RentalNotFoundException se nenhum aluguel for encontrado.
     */
    @GetMapping("/game-id/{id}")
    public ResponseEntity<List<RentalDto>> listRentalsByGameId(@PathVariable("id") Long gameId) {
        return ResponseEntity.ok(rentalService.listRentalsByGameId(gameId));
    }

    /**
     * Endpoint para listar aluguéis filtrados pelo ID do usuário.
     *
     * @param userId identificador do usuário.
     * @return {@link ResponseEntity} contendo uma lista de {@link RentalDto}.
     * @throws RentalNotFoundException se nenhum aluguel for encontrado.
     */
    @GetMapping("/user-id/{id}")
    public ResponseEntity<List<RentalDto>> listRentalsByUserId(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(rentalService.listRentalsByUserId(userId));
    }

    /**
     * Endpoint para listar aluguéis filtrados pela data de início.
     *
     * @param rentalDate data de início do aluguel.
     * @return {@link ResponseEntity} contendo uma lista de {@link RentalDto}.
     * @throws RentalNotFoundException se nenhum aluguel for encontrado.
     */
    @GetMapping(params = "rental-date")
    public ResponseEntity<List<RentalDto>> listRentalsByRentalDate(@RequestParam("rental-date") LocalDate rentalDate) {
        return ResponseEntity.ok(rentalService.listRentalsByRentalDate(rentalDate));
    }

    /**
     * Endpoint para listar aluguéis filtrados pela data de encerramento.
     *
     * @param endDate data de encerramento do aluguel.
     * @return {@link ResponseEntity} contendo uma lista de {@link RentalDto}.
     * @throws RentalNotFoundException se nenhum aluguel for encontrado.
     */
    @GetMapping(params = "end-date")
    public ResponseEntity<List<RentalDto>> listRentalsByReturnDate(@RequestParam("end-date") LocalDate endDate) {
        return ResponseEntity.ok(rentalService.listRentalsByEndDate(endDate));
    }

    /**
     * Endpoint para listar aluguéis filtrados pelo status.
     *
     * @param rentalStatus status do aluguel, conforme o enum {@link RentalStatus}.
     * @return {@link ResponseEntity} contendo uma lista de {@link RentalDto}.
     * @throws RentalNotFoundException se nenhum aluguel for encontrado.
     */
    @GetMapping(params = "status")
    public ResponseEntity<List<RentalDto>> listRentalsByStatus(@RequestParam("status") RentalStatus rentalStatus) {
        return ResponseEntity.ok(rentalService.listRentalsByStatus(rentalStatus));
    }

    /**
     * Endpoint para listar aluguéis filtrados pelo nome do usuário (username).
     *
     * @param userName nome do usuário ou parte dele.
     * @return {@link ResponseEntity} contendo uma lista de {@link RentalDto}.
     * @throws RentalNotFoundException se nenhum aluguel for encontrado.
     */
    @GetMapping(params = "username")
    public ResponseEntity<List<RentalDto>> listRentalsByUserName(@RequestParam("username") String userName) {
        return ResponseEntity.ok(rentalService.listRentalsByUserName(userName));
    }

    /**
     * Endpoint para listar aluguéis filtrados pelo título do jogo.
     *
     * @param gameTitle título do jogo ou parte dele.
     * @return {@link ResponseEntity} contendo uma lista de {@link RentalDto}.
     * @throws RentalNotFoundException se nenhum aluguel for encontrado.
     */
    @GetMapping(params = "title")
    public ResponseEntity<List<RentalDto>> listRentalsByGameTitle(@RequestParam("title") String gameTitle) {
        return ResponseEntity.ok(rentalService.listRentalsByGameTitle(gameTitle));
    }
}