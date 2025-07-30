package dev.viniciussr.gamerental.service;

import dev.viniciussr.gamerental.dto.RentalDto;
import dev.viniciussr.gamerental.dto.RentalUpdateDto;
import dev.viniciussr.gamerental.enums.RentalStatus;
import dev.viniciussr.gamerental.exception.game.GameNotFoundException;
import dev.viniciussr.gamerental.exception.rental.RentalAlreadyClosedException;
import dev.viniciussr.gamerental.exception.rental.RentalNotFoundException;
import dev.viniciussr.gamerental.exception.user.UserNotFoundException;
import dev.viniciussr.gamerental.model.Game;
import dev.viniciussr.gamerental.model.Rental;
import dev.viniciussr.gamerental.model.User;
import dev.viniciussr.gamerental.repository.GameRepository;
import dev.viniciussr.gamerental.repository.RentalRepository;
import dev.viniciussr.gamerental.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    private final UserService userService;
    private final GameService gameService;

    public RentalService(
            RentalRepository rentalRepository,
            GameRepository gameRepository,
            UserRepository userRepository,
            UserService userService,
            GameService gameService
    ) {
        this.rentalRepository = rentalRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.gameService = gameService;
    }

    // -------------------- CRUD BÁSICO --------------------

    // Cria um novo aluguel
    public RentalDto createRental(RentalDto dto) {

        Game game = gameRepository.findById(dto.gameId())
                .orElseThrow(() -> new GameNotFoundException("Jogo não encontrado no id: " + dto.gameId()));

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado no id: " + dto.userId()));

        gameService.validateIfGameIsAvailable(game);
        userService.validateUserRentalLimit(user);

        LocalDate today = LocalDate.now();

        Rental rental = new Rental(
                game,
                user,
                today,
                today.plusDays(15), // Duração padrão de um aluguel: 15 dias
                RentalStatus.ACTIVE
        );

        gameService.updateGameQuantityAndAvailability(game, -1);
        userService.updateUserActiveRentalsCount(user, 1);

        return new RentalDto(rentalRepository.save(rental));
    }

    // Atualiza um aluguel existente
    public RentalDto updateRental(Long id, RentalUpdateDto dto) {

        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException("Aluguel não encontrado no id: " + id));

        if (rental.getStatus() == RentalStatus.RETURNED) {
            throw new RentalAlreadyClosedException("Não é possível alterar um aluguel já devolvido.");
        }

        if (dto.gameId() != null) {
            Game game = gameRepository.findById(dto.gameId())
                    .orElseThrow(() -> new GameNotFoundException("Jogo não encontrado no id: " + dto.gameId()));

            rental.setGame(game);
        }

        if (dto.userId() != null) {
            User user = userRepository.findById(dto.userId())
                    .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado no id: " + dto.userId()));

            rental.setUser(user);
        }

        return new RentalDto(rentalRepository.save(rental));
    }

    // Deleta um aluguel existente
    public void deleteRental(Long id) {


        Rental deletedRental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException("Aluguel não encontrado no id: " + id));

        rentalRepository.delete(deletedRental);
    }

    // --------------- FILTROS ---------------

    // Busca um aluguel pelo ID
    public RentalDto findRentalById(Long id) {

        return rentalRepository.findById(id)
                .map(RentalDto::new)
                .orElseThrow(() -> new RentalNotFoundException("Aluguel não encontrado no id: " + id));
    }

    // Lista todos os aluguéis cadastrados
    public List<RentalDto> listRentals() {

        List<RentalDto> loans = rentalRepository.findAll()
                .stream()
                .map(RentalDto::new)
                .toList();
        if (loans.isEmpty()) {
            throw new RentalNotFoundException("Nenhum aluguel cadastrado no momento");
        }
        return loans;
    }

    // Lista aluguéis por ID do jogo
    public List<RentalDto> listRentalsByGameId(Long idGame) {

        List<RentalDto> rentals = rentalRepository.findByGame_IdGame(idGame)
                .stream()
                .map(RentalDto::new)
                .toList();
        if (rentals.isEmpty()) {
            throw new RentalNotFoundException("Nenhum aluguel encontrado para o jogo no id: " + idGame);
        }
        return rentals;
    }

    // Lista aluguéis por ID do usuário
    public List<RentalDto> listRentalsByUserId(Long idUser) {

        List<RentalDto> rentals = rentalRepository.findByUser_IdUser(idUser)
                .stream()
                .map(RentalDto::new)
                .toList();
        if (rentals.isEmpty()) {
            throw new RentalNotFoundException("Nenhum aluguel encontrado para o usuário no id: " + idUser);
        }
        return rentals;
    }

    // Lista aluguéis por data de início
    public List<RentalDto> listRentalsByLoanDate(LocalDate rentalDate) {

        List<RentalDto> rentals = rentalRepository.findByRentalDate(rentalDate)
                .stream()
                .map(RentalDto::new)
                .toList();
        if (rentals.isEmpty()) {
            throw new RentalNotFoundException("Nenhum aluguel encontrado na seguinte data: " + rentalDate);
        }
        return rentals;
    }

    // Lista aluguéis por data de devolução
    public List<RentalDto> listRentalsByReturnDate(LocalDate returnDate) {

        List<RentalDto> rentals = rentalRepository.findByEndDate(returnDate)
                .stream()
                .map(RentalDto::new)
                .toList();
        if (rentals.isEmpty()) {
            throw new RentalNotFoundException("Nenhum aluguel encontrado na seguinte data: " + returnDate);
        }
        return rentals;
    }

    // Lista aluguéis pelo status
    public List<RentalDto> listRentalsByStatus(RentalStatus rentalStatus) {

        List<RentalDto> rentals = rentalRepository.findByStatus(rentalStatus)
                .stream()
                .map(RentalDto::new)
                .toList();
        if (rentals.isEmpty()) {
            throw new RentalNotFoundException("Nenhum aluguel encontrado com o status: " + rentalStatus.name());
        }
        return rentals;
    }

    // Lista aluguéis por nome de usuário
    public List<RentalDto> listRentalsByUserName(String userName) {

        List<RentalDto> rentals =  rentalRepository.findByUser_Name(userName)
                .stream()
                .map(RentalDto::new)
                .toList();
        if (rentals.isEmpty()) {
            throw new RentalNotFoundException("Nenhum aluguel encontrado para o usuário: " + userName);
        }
        return rentals;
    }

    // Lista aluguéis pelo título do jogo
    public List<RentalDto> listRentalsByGameTitle(String gameTitle) {

        List<RentalDto> rentals =   rentalRepository.findByGame_Title(gameTitle)
                .stream()
                .map(RentalDto::new)
                .toList();
        if (rentals.isEmpty()) {
            throw new RentalNotFoundException("Nenhum aluguel encontrado para o jogo: " + gameTitle);
        }
        return rentals;
    }

    // --------------- MÉTODOS UTILITÁRIOS ---------------

    // Valida se o aluguel está ativo
    // (Para utilização nos métodos: return, renew e cancel)
    private Rental getActiveRental(Long id) {

        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException("Aluguel não encontrado no id: " + id));

        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new RentalAlreadyClosedException("Este aluguel já está encerrado. id: " + rental.getIdRental());
        }

        return rental;
    }

    // Devolve um aluguel
    public void returnRental(Long id) {

        Rental rental = getActiveRental(id);

        rental.setStatus(RentalStatus.RETURNED);
        rental.setEndDate(LocalDate.now());

        gameService.updateGameQuantityAndAvailability(rental.getGame(), 1);
        userService.updateUserActiveRentalsCount(rental.getUser(), -1);

        rentalRepository.save(rental);
    }

    // Renova um aluguel por mais 7 dias
    public void renewRental(Long id) {

        Rental rental = getActiveRental(id);
        rental.setEndDate(rental.getEndDate().plusDays(7));

        rentalRepository.save(rental);
    }

    // Cancela um aluguel
    public Rental cancelRental(Long id) {

        Rental rental = getActiveRental(id);

        rental.setStatus(RentalStatus.CANCELLED);
        rental.setEndDate(LocalDate.now());

        gameService.updateGameQuantityAndAvailability(rental.getGame(), 1);
        userService.updateUserActiveRentalsCount(rental.getUser(), -1);

        rentalRepository.save(rental);

        return rental;
    }

    // Atualiza status de aluguel para atrasado (LATE)
    public void markRentalsLate() {

        List<Rental> activeRentals = rentalRepository.findByStatus(RentalStatus.ACTIVE);

        for (Rental rental : activeRentals) {
            if (rental.getRentalDate().plusDays(15).isBefore(LocalDate.now())) {
                rental.setStatus(RentalStatus.LATE);
                rentalRepository.save(rental);
            }
        }
    }

    // Verifica e atualiza atraso dos aluguéis
    @Scheduled(cron = "0 0 0 * * *") // *** Verificação feita diariamente às 00h ***
    public void checkForLateRentals() {

        markRentalsLate();
    }
}
