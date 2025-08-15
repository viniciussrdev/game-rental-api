package dev.viniciussr.gamerental.service;

import dev.viniciussr.gamerental.dto.RentalDto;
import dev.viniciussr.gamerental.dto.RentalUpdateDto;
import dev.viniciussr.gamerental.enums.RentalStatus;
import dev.viniciussr.gamerental.exception.game.GameIsNotAvailableException;
import dev.viniciussr.gamerental.exception.game.GameNotFoundException;
import dev.viniciussr.gamerental.exception.rental.PlanLimitExceededException;
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

/**
 * Serviço responsável por gerenciar operações relacionadas aos aluguéis da aplicação.
 * <p>
 * Inclui criação, atualização, exclusão, busca e regras de negócio.
 * </p>
 */
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

    // ******************************
    // CRUD BÁSICO
    // ******************************

    /**
     * Cria um novo aluguel no sistema.
     * <p>
     * Aluguel é criado a partir dos IDs de jogo e usuário informados,
     * com data de início como 'hoje', data prevista de devolução em 15 dias e status ATIVO.
     * Aplica validações de disponibilidade do jogo e limite de aluguéis do usuário.
     * </p>
     *
     * @param dto objeto com os dados do aluguel a ser criado.
     * @return  DTO do aluguel criado ({@link RentalDto}).
     * @throws GameNotFoundException        se o jogo não for encontrado.
     * @throws UserNotFoundException        se o usuário não for encontrado.
     * @throws GameIsNotAvailableException  se o jogo estiver indisponível.
     * @throws PlanLimitExceededException   se o usuário tiver excedido o limite do plano.
     */
    public RentalDto createRental(RentalDto dto) {

        Game game = gameRepository.findById(dto.gameId())
                .orElseThrow(() -> new GameNotFoundException("Jogo não encontrado no id: " + dto.gameId()));

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado no id: " + dto.userId()));

        gameService.validateIfGameIsAvailable(game); // Valida se o jogo está disponível para aluguel
        userService.validateUserRentalLimit(user); // Valida se o limite de aluguéis ativos do usuário foi excedido

        LocalDate today = LocalDate.now();

        Rental rental = new Rental(
                game,
                user,
                today, // Data de início: hoje (data atual)
                today.plusDays(15), // Data prevista para devolução: 15 dias a partir de 'hoje'
                RentalStatus.ACTIVE // Ativo
        );

        gameService.updateGameQuantityAndAvailability(game, -1); // Atualiza quantidade do jogo na loja (-1)
        userService.updateUserActiveRentalsCount(user, 1); // Atualiza contagem de aluguéis ativos do usuário (+1)

        return new RentalDto(rentalRepository.save(rental));
    }

    /**
     * Atualiza os dados de um aluguel existente (correção de jogo/usuário).
     * <p>
     * Apenas os campos não nulos no DTO serão atualizados.
     * Não permite alterações se o aluguel já estiver encerrado (RETURNED ou CANCELLED).
     * </p>
     *
     * @param id  ID do aluguel.
     * @param dto DTO com os dados de atualização.
     * @return DTO do aluguel atualizado ({@link RentalDto}).
     * @throws RentalNotFoundException      se o aluguel não for encontrado.
     * @throws RentalAlreadyClosedException se o aluguel já estiver encerrado.
     * @throws GameNotFoundException        se o novo jogo (quando informado) não for encontrado.
     * @throws UserNotFoundException        se o novo usuário (quando informado) não for encontrado.
     */
    public RentalDto updateRental(Long id, RentalUpdateDto dto) {

        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException("Aluguel não encontrado no id: " + id));

        // Verifica se o aluguel a ser atualizado já foi encerrado
        if (rental.getStatus() == RentalStatus.RETURNED || rental.getStatus() == RentalStatus.CANCELLED ) {
            throw new RentalAlreadyClosedException("Não é possível alterar um aluguel já encerrado.");
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

    /**
     * Remove um aluguel existente do sistema.
     *
     * @param id ID do aluguel a ser deletado.
     * @throws RentalNotFoundException se o aluguel não for encontrado.
     */
    public void deleteRental(Long id) {


        Rental deletedRental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException("Aluguel não encontrado no id: " + id));

        rentalRepository.delete(deletedRental);
    }

    // ******************************
    // MÉTODOS DE BUSCA
    // ******************************

    /**
     * Busca um aluguel pelo seu ID.
     *
     * @param id ID do aluguel.
     * @return DTO do aluguel encontrado pelo ID ({@link RentalDto}).
     * @throws RentalNotFoundException se o aluguel não for encontrado.
     */
    public RentalDto findRentalById(Long id) {

        return rentalRepository.findById(id)
                .map(RentalDto::new)
                .orElseThrow(() -> new RentalNotFoundException("Aluguel não encontrado no id: " + id));
    }

    /**
     * Lista todos os aluguéis cadastrados.
     *
     * @return Lista de todos os aluguéis {@link RentalDto}.
     * @throws RentalNotFoundException se não houver aluguéis cadastrados.
     */
    public List<RentalDto> listRentals() {

        List<RentalDto> rentals = rentalRepository.findAll()
                .stream()
                .map(RentalDto::new)
                .toList();
        if (rentals.isEmpty()) {
            throw new RentalNotFoundException("Nenhum aluguel cadastrado no momento");
        }

        return rentals;
    }

    /**
     * Lista aluguéis pelo ID do jogo.
     *
     * @param idGame ID do jogo.
     * @return Lista de aluguéis pelo ID do jogo informado ({@link RentalDto}).
     * @throws RentalNotFoundException se nenhum aluguel for encontrado.
     */
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

    /**
     * Lista aluguéis pelo ID do usuário.
     *
     * @param idUser ID do usuário.
     * @return Lista de aluguéis pelo ID do usuário informado ({@link RentalDto}).
     * @throws RentalNotFoundException se nenhum aluguel for encontrado.
     */
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

    /**
     * Lista aluguéis pela data de início.
     *
     * @param rentalDate data de início do aluguel.
     * @return Lista de aluguéis pela data de início informada ({@link RentalDto}).
     * @throws RentalNotFoundException se nenhum aluguel for encontrado.
     */
    public List<RentalDto> listRentalsByRentalDate(LocalDate rentalDate) {

        List<RentalDto> rentals = rentalRepository.findByRentalDate(rentalDate)
                .stream()
                .map(RentalDto::new)
                .toList();
        if (rentals.isEmpty()) {
            throw new RentalNotFoundException("Nenhum aluguel encontrado na seguinte data: " + rentalDate);
        }
        return rentals;
    }

    /**
     * Lista aluguéis pela data de encerramento prevista.
     *
     * @param endDate data de encerramento do aluguel.
     * @return Lista de aluguéis pela data de encerramento informada ({@link RentalDto}).
     * @throws RentalNotFoundException se nenhum aluguel for encontrado.
     */
    public List<RentalDto> listRentalsByEndDate(LocalDate endDate) {

        List<RentalDto> rentals = rentalRepository.findByEndDate(endDate)
                .stream()
                .map(RentalDto::new)
                .toList();
        if (rentals.isEmpty()) {
            throw new RentalNotFoundException("Nenhum aluguel encontrado na seguinte data: " + endDate);
        }
        return rentals;
    }

    /**
     * Lista aluguéis pelo seu status.
     *
     * @param rentalStatus status do alguel.
     * @return Lista de aluguéis pelo status informado ({@link RentalDto}).
     * @throws RentalNotFoundException se nenhum aluguel for encontrado.
     */
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

    /**
     * Lista aluguéis pelo nome de usuário (username).
     *
     * @param userName nome de usuário.
     * @return Lista de aluguéis pelo username informado ({@link RentalDto}).
     * @throws RentalNotFoundException se nenhum aluguel for encontrado.
     */
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

    /**
     * Lista aluguéis pelo título do jogo.
     *
     * @param gameTitle título do jogo.
     * @return Lista de aluguéis pelo título informado ({@link RentalDto}).
     * @throws RentalNotFoundException se nenhum aluguel for encontrado.
     */
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

    // ******************************
    // LÓGICA DE NEGÓCIO
    // ******************************

    /**
     * Obtém um aluguel ativo (status {@link RentalStatus#ACTIVE}) pelo ID.
     * <p>
     * Utilizado por {@link #returnRental(Long)}, {@link #renewRental(Long)} e {@link #cancelRental(Long)}.
     * </p>
     *
     * @param id ID do aluguel.
     * @return Entidade {@link Rental} ativa.
     * @throws RentalNotFoundException       se o aluguel não for encontrado.
     * @throws RentalAlreadyClosedException  se o aluguel não estiver ativo.
     */
    private Rental getActiveRental(Long id) {

        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException("Aluguel não encontrado no id: " + id));

        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new RentalAlreadyClosedException("Este aluguel já está encerrado. id: " + rental.getIdRental());
        }

        return rental;
    }

    /**
     * Realiza a devolução de um aluguel.
     * <p>
     * Define status como {@link RentalStatus#RETURNED}, data de encerramento como 'hoje'
     * e atualiza quantidade do jogo e contador de aluguéis ativos do usuário.
     * </p>
     *
     * @param id ID do aluguel.
     * @throws RentalNotFoundException       se o aluguel não for encontrado.
     * @throws RentalAlreadyClosedException  se o aluguel não estiver ativo.
     */
    public void returnRental(Long id) {

        Rental rental = getActiveRental(id); // Verifica se o aluguel está ativo

        rental.setStatus(RentalStatus.RETURNED); // Define status do aluguel como 'RETURNED' (devolvido)
        rental.setEndDate(LocalDate.now()); // Define data de encerramento como 'hoje' (data atual)

        gameService.updateGameQuantityAndAvailability(rental.getGame(), 1); // Atualiza quantidade do jogo
        userService.updateUserActiveRentalsCount(rental.getUser(), -1); // Atualiza contador de aluguéis ativos

        rentalRepository.save(rental);
    }

    /**
     * Renova um aluguel ativo por mais 7 dias.
     *
     * @param id ID do aluguel.
     * @throws RentalNotFoundException       se o aluguel não for encontrado.
     * @throws RentalAlreadyClosedException  se o aluguel não estiver ativo.
     */
    public void renewRental(Long id) {

        Rental rental = getActiveRental(id); // Verifica se o aluguel está ativo
        rental.setEndDate(rental.getEndDate().plusDays(7)); // Acrescenta 7 dias à data de devolução

        rentalRepository.save(rental);
    }

    /**
     * Cancela um aluguel ativo.
     * <p>
     * Define status como {@link RentalStatus#CANCELLED}, data de encerramento como 'hoje'
     * e atualiza quantidade do jogo e contador de aluguéis ativos do usuário.
     * </p>
     *
     * @param id ID do aluguel.
     * @throws RentalNotFoundException       se o aluguel não for encontrado.
     * @throws RentalAlreadyClosedException  se o aluguel não estiver ativo.
     */
    public void cancelRental(Long id) {

        Rental rental = getActiveRental(id); // Verifica se o aluguel está ativo

        rental.setStatus(RentalStatus.CANCELLED); // Define status do aluguel como 'CANCELLED' (cancelado)
        rental.setEndDate(LocalDate.now()); // Define data de encerramento como 'hoje' (data atual)

        gameService.updateGameQuantityAndAvailability(rental.getGame(), 1); // Atualiza quantidade do jogo
        userService.updateUserActiveRentalsCount(rental.getUser(), -1); // Atualiza contador de aluguéis ativos

        rentalRepository.save(rental);
    }

    /**
     * Marca como atrasados ({@link RentalStatus#LATE}) todos os aluguéis ativos
     * cuja data de início seja superior a 15 dias da data atual.
     */
    public void markRentalsLate() {

        List<Rental> activeRentals = rentalRepository.findByStatus(RentalStatus.ACTIVE);

        for (Rental rental : activeRentals) {
            if (rental.getRentalDate().plusDays(15).isBefore(LocalDate.now())) {
                rental.setStatus(RentalStatus.LATE);

                rentalRepository.save(rental);
            }
        }
    }

    /**
     * Tarefa agendada para verificar diariamente (à meia-noite) aluguéis em atraso
     * e atualizá-los para {@link RentalStatus#LATE} quando aplicável.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void checkForLateRentals() {

        markRentalsLate();
    }
}