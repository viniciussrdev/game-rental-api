package dev.viniciussr.gamerental.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dev.viniciussr.gamerental.exception.game.GameIsNotAvailableException;
import dev.viniciussr.gamerental.exception.game.GameNotFoundException;
import dev.viniciussr.gamerental.exception.jwt.*;
import dev.viniciussr.gamerental.exception.rental.PlanLimitExceededException;
import dev.viniciussr.gamerental.exception.rental.RentalAlreadyClosedException;
import dev.viniciussr.gamerental.exception.rental.RentalNotFoundException;
import dev.viniciussr.gamerental.exception.user.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

// Classe responsável por capturar e tratar exceções de forma global
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Constrói uma resposta de erro padronizada para as exceções lançadas na API, através do modelo ErrorResponse
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }

    // Captura exceções genéricas inesperadas
    // Retorna status 500 (INTERNAL SERVER ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado: " + e.getMessage());
    }

    // Captura exceções de regra de negócio
    // Retorna status 400 (BAD REQUEST)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // Captura exceção: jogo não encontrado
    // Retorna status 404 (NOT FOUND)
    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGameNotFound(GameNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // Captura exceção: usuário não encontrado
    // Retorna status 404 (NOT FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // Captura exceção: aluguel não encontrado
    // Retorna status 404 (NOT FOUND)
    @ExceptionHandler(RentalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRentalNotFound(RentalNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // Captura exceção: jogo indisponível para aluguel (conflito de estado)
    // Retorna status 409 (CONFLICT)
    @ExceptionHandler(GameIsNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleGameIsNotAvailable(GameIsNotAvailableException e) {
        return buildErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    // Captura exceção: limite de aluguéis ativos por plano excedido
    // Retorna status 422 (UNPROCESSABLE ENTITY)
    @ExceptionHandler(PlanLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handlePlanLimitExceeded(PlanLimitExceededException e) {
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }

    // Captura tentativa de devolução de jogos já devolvidos (conflito de estado)
    // Retorna status 409 (CONFLICT)
    @ExceptionHandler(RentalAlreadyClosedException.class)
    public ResponseEntity<ErrorResponse> handleRentalAlreadyClosed(RentalAlreadyClosedException e) {
        return buildErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    // Captura tentativa de cadastro de um usuário com dados já existentes (duplicação)
    // Retorna status 409 (CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(DataIntegrityViolationException e) {
        String message = "Usuário já cadastrado";
        return buildErrorResponse(HttpStatus.CONFLICT, message);
    }

    // Captura exceções de validação de argumentos recebidos (bean validation)
    // Retorna status 400 (BAD REQUEST) com os campos inválidos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> "[" + error.getField() + "] : " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    // Captura incompatibilidade de tipo nos parâmetros da URL ou query (ex: String em campo Long)
    // Retorna status 400 (BAD REQUEST) com mensagem informando o tipo esperado para o parâmetro
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String paramName = e.getName();
        String expectedType = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "o tipo correto";
        String message = "O parâmetro " + paramName + " é inválido. Esperado: " + expectedType;

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    // Captura exceções de leitura da requisição (JSON inválido, mal formatado, etc)
    // Se a causa for InvalidFormatException, direciona para 'handlerInvalidFormat'
    // Caso contrário, retorna erro genérico de formatação (BAD REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException invalidFormat) {
            return handleInvalidFormat(invalidFormat);
        }
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Erro de leitura dos dados enviados na requisição");
    }

    // Captura exceções de formatação no JSON (foco em tratamento de enums inválidos)
    // Se a causa for enum inválido, retorna status 400 (BAD REQUEST) + lista de valores aceitos
    // Caso contrário, retorna erro genérico de formatação
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormat(InvalidFormatException e) {

        if (e.getTargetType().isEnum()) {
            // Enum inválido
            String values = Arrays.stream(e.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            String message = "Valor inválido: '" + e.getValue() + "'. Aceitos: " + values;
            return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
        }
        // Erro genérico de formatação
        String message = "Formato inválido no corpo da requisição";

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    // Captura tentativa de login com credenciais inválidas
    // Retorna status 401 (UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException e) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
    }

    // Captura exceção: erro ao gerar token JWT
    // Retorna status 500 (INTERNAL SERVER ERROR)
    @ExceptionHandler(JwtGenerationException.class)
    public ResponseEntity<ErrorResponse> handleJwtGenerationException(JwtGenerationException e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    // Captura exceção: erro ao validar token JWT → expirado
    // Retorna status 401 (UNAUTHORIZED)
    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleExpiredToken(JwtTokenExpiredException e) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    // Captura exceção: erro ao validar token JWT → assinatura inválida
    // Retorna status 401 (UNAUTHORIZED)
    @ExceptionHandler(JwtSignatureVerificationException.class)
    public ResponseEntity<ErrorResponse> handleSignatureVerification(JwtSignatureVerificationException e) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    // Captura exceção: erro ao validar token JWT → algoritmo inválido
    // Retorna status 401 (UNAUTHORIZED)
    @ExceptionHandler(JwtAlgorithmMismatchException.class)
    public ResponseEntity<ErrorResponse> handleAlgorithmMismatch(JwtAlgorithmMismatchException e) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    // Captura exceção: erro ao validar token JWT → falha na verificação
    // Retorna status 401 (UNAUTHORIZED)
    @ExceptionHandler(JwtVerificationException.class)
    public ResponseEntity<ErrorResponse> handleJwtVerification(JwtVerificationException e) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }
}