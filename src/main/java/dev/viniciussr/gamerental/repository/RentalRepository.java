package dev.viniciussr.gamerental.repository;

import dev.viniciussr.gamerental.enums.RentalStatus;
import dev.viniciussr.gamerental.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

// Repositório responsável pelas operações de acesso a dados da entidade Rental
// Estende JpaRepository para fornecer funcionalidades básicas de CRUD
public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByGame_IdGame(Long IdGame);

    List<Rental> findByUser_IdUser(Long IdUser);

    List<Rental> findByRentalDate(LocalDate rentalDate);

    List<Rental> findByEndDate(LocalDate endDate);

    List<Rental> findByStatus(RentalStatus status);

    List<Rental> findByUser_Name(String userName);

    List<Rental> findByGame_Title(String gameTitle);
}