package dev.viniciussr.gamerental.repository;

import dev.viniciussr.gamerental.enums.RentalStatus;
import dev.viniciussr.gamerental.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositório responsável pelas operações de acesso a dados da entidade {@link Rental}.
 * <p>
 * Estende {@link JpaRepository} para fornecer funcionalidades básicas de CRUD.
 * </p>
 */
public interface RentalRepository extends JpaRepository<Rental, Long> {

    /**
     * Lista os aluguéis de um jogo específico pelo ID do jogo.
     *
     * @param IdGame ID do jogo.
     * @return Lista de aluguéis filtrada pelo jogo informado.
     */
    List<Rental> findByGame_IdGame(Long IdGame);

    /**
     * Lista os aluguéis de um usuário específico pelo ID do usuário.
     *
     * @param IdUser ID do usuário.
     * @return Lista de aluguéis filtrada pelo usuário informado.
     */
    List<Rental> findByUser_IdUser(Long IdUser);

    /**
     * Lista os aluguéis realizados em uma data específica.
     *
     * @param rentalDate data do aluguel.
     * @return Lista de aluguéis filtrada pela data de início.
     */
    List<Rental> findByRentalDate(LocalDate rentalDate);

    /**
     * Lista os aluguéis que terminam em uma data específica.
     *
     * @param endDate data de término do aluguel.
     * @return Lista de aluguéis filtrada pela data de encerramento.
     */
    List<Rental> findByEndDate(LocalDate endDate);

    /**
     * Lista os aluguéis pelo status informado.
     *
     * @param status status do aluguel.
     * @return Lista de aluguéis filtrada pelo status informado.
     */
    List<Rental> findByStatus(RentalStatus status);

    /**
     * Lista os aluguéis de um usuário específico pelo nome do usuário.
     *
     * @param userName nome do usuário.
     * @return Lista de aluguéis filtrada pelo usuário informado.
     */
    List<Rental> findByUser_Name(String userName);

    /**
     * Lista os aluguéis de um jogo específico pelo título do jogo.
     *
     * @param gameTitle título do jogo.
     * @return Lista de aluguéis filtrada pelo jogo informado.
     */
    List<Rental> findByGame_Title(String gameTitle);
}