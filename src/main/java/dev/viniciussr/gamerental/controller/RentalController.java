package dev.viniciussr.gamerental.controller;

import dev.viniciussr.gamerental.dto.RentalDto;
import dev.viniciussr.gamerental.enums.RentalStatus;
import dev.viniciussr.gamerental.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rentals") // Rota base para as requisições deste controller
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    // Endpoint para criar novo aluguel
    @PostMapping
    public ResponseEntity<RentalDto> createRental(@Valid @RequestBody RentalDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalService.createRental(dto));
    }

    // Endpoint para atualizar alguel existente
    @PutMapping("/{id}")
    public ResponseEntity<RentalDto> updateRental(@PathVariable Long id, @Valid @RequestBody RentalDto dto) {
        return ResponseEntity.ok(rentalService.updateRental(id, dto));
    }

    // Endpoint para deletar aluguel do banco de dados
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
        rentalService.deleteRental(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para devolver aluguel ativo
    @PutMapping("/return/{id}")
    public ResponseEntity<Void> returnRental(@PathVariable Long id) {
        rentalService.returnRental(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para renovar aluguel por 7 dias
    @PutMapping("/renew-rental/{id}")
    public ResponseEntity<Void> renewRental(@PathVariable Long id) {
        rentalService.renewRental(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para buscar aluguel por ID
    @GetMapping("/{id}")
    public ResponseEntity<RentalDto> findRentalById(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.findRentalById(id));
    }

    // Endpoint para listar todos os aluguéis
    @GetMapping
    public ResponseEntity<List<RentalDto>> listRentals() {
        return ResponseEntity.ok(rentalService.listRentals());
    }

    // Endpoint para listar aluguéis por ID do jogo
    @GetMapping("/game-id/{id}")
    public ResponseEntity<List<RentalDto>> listRentalsByGameId(@PathVariable("id") Long gameId) {
        return ResponseEntity.ok(rentalService.listRentalsByGameId(gameId));
    }

    // Endpoint para listar aluguéis por Id do usuário
    @GetMapping("/user-id/{id}")
    public ResponseEntity<List<RentalDto>> listRentalsByUserId(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(rentalService.listRentalsByUserId(userId));
    }

    // Endpoint para listar aluguéis por data de início
    @GetMapping(params = "rental-date")
    public ResponseEntity<List<RentalDto>> listRentalsByRentalDate(@RequestParam("rental-date") LocalDate rentalDate) {
        return ResponseEntity.ok(rentalService.listRentalsByLoanDate(rentalDate));
    }

    // Endpoint para listar aluguéis por data de devolução
    @GetMapping(params = "return-date")
    public ResponseEntity<List<RentalDto>> listRentalsByReturnDate(@RequestParam("return-date") LocalDate returnDate) {
        return ResponseEntity.ok(rentalService.listRentalsByReturnDate(returnDate));
    }

    // Endpoint para listar aluguéis por status
    @GetMapping(params = "status")
    public ResponseEntity<List<RentalDto>> listRentalsByStatus(@RequestParam("status") RentalStatus rentalStatus) {
        return ResponseEntity.ok(rentalService.listRentalsByStatus(rentalStatus));
    }

    // Endpoint para listar aluguéis por nome de usuário
    @GetMapping(params = "username")
    public ResponseEntity<List<RentalDto>> listRentalsByUserName(@RequestParam("username") String userName) {
        return ResponseEntity.ok(rentalService.listRentalsByUserName(userName));
    }

    // Endpoint para listar aluguéis por título do jogo
    @GetMapping(params = "title")
    public ResponseEntity<List<RentalDto>> listRentalsByGameTitle(@RequestParam("title") String gameTitle) {
        return ResponseEntity.ok(rentalService.listRentalsByGameTitle(gameTitle));
    }
}
