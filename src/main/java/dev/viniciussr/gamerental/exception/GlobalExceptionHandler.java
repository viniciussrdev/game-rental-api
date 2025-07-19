package dev.viniciussr.gamerental.exception;

import dev.viniciussr.gamerental.exception.game.*;
import dev.viniciussr.gamerental.exception.rental.*;
import dev.viniciussr.gamerental.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

// Captura e tratamento de exceções globais da API
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Cria uma resposta padronizada com status, erro, mensagem e timestamp
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }

    // Handler para exceções não previstas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado: " + ex.getMessage()); // 500
    }

    // Handler para exceções de regra de negócio
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()); // 400
    }

    // Handler para jogo não encontrado
    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGameNotFound(GameNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage()); // 404
    }

    // Handler para usuário não encontrado
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage()); // 404
    }

    // Handler para aluguel não encontrado
    @ExceptionHandler(RentalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRentalNotFound(RentalNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage()); // 404
    }

    // Handler para jogo não disponível para aluguel
    @ExceptionHandler(GameIsNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleGameUnavailable(GameIsNotAvailableException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage()); // 409
    }

    // Handler para quando usuário excede limite de aluguéis do plano
    @ExceptionHandler(PlanLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handlePlanLimitExceeded(PlanLimitExceededException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage()); // 422
    }

    // Handler para devolução de jogos já devolvidos
    @ExceptionHandler(RentalAlreadyReturnedException.class)
    public ResponseEntity<ErrorResponse> handleRentalAlreadyReturned(RentalAlreadyReturnedException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage()); // 409
    }
}
