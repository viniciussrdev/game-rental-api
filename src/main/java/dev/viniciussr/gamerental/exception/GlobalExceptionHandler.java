package dev.viniciussr.gamerental.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dev.viniciussr.gamerental.exception.game.*;
import dev.viniciussr.gamerental.exception.rental.*;
import dev.viniciussr.gamerental.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

// Captura e tratamento de exceções globais da API
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Constrói respostas de erro padronizadas com 'status', 'error', 'message' e 'timestamp'
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }

    // Handler genérico para exceções inesperadas
    // Retorna status 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado: " + ex.getMessage());
    }

    // Handler para exceções de regra de negócio
    // Retorna status 400
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Handler para exceção de jogo não encontrado
    // Retorna status 404
    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGameNotFound(GameNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Handler para exceção de usuário não encontrado
    // Retorna status 404
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Handler para exceção de aluguel não encontrado
    // Retorna status 404
    @ExceptionHandler(RentalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRentalNotFound(RentalNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Handler para exceção de jogo indisponível para aluguel (conflito de estado)
    // Retorna status 409
    @ExceptionHandler(GameIsNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleGameUnavailable(GameIsNotAvailableException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // Handler para exceção de usuário excedendo limite de aluguéis do plano
    // Retorna status 422
    @ExceptionHandler(PlanLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handlePlanLimitExceeded(PlanLimitExceededException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    // Handler para tentativa de devolução de jogos já devolvidos (conflito de estado)
    // Retorna status 409
    @ExceptionHandler(RentalAlreadyReturnedException.class)
    public ResponseEntity<ErrorResponse> handleRentalAlreadyReturned(RentalAlreadyReturnedException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // Handler para validação de argumentos recebidos (bean validation)
    // Retorna status 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> "[" + error.getField() + "] : " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    // Handler para erros de formatação no JSON
    // Se erro for causado por enum inválido, retorna status 400 + lista de valores aceitos
    // Se não for enum, retorna erro genérico de formatação (400)
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormat(InvalidFormatException ex) {
        if (ex.getTargetType().isEnum()) {
            // Enum inválido
            String values = Arrays.stream(ex.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            String message = "Valor inválido: '" + ex.getValue() + "'. Aceitos: " + values;
            return buildResponse(HttpStatus.BAD_REQUEST, message);
        }

        // Erro genérico
        String message = "Formato inválido no corpo da requisição.";
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }
}