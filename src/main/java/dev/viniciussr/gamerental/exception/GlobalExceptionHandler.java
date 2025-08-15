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

/**
 * Classe responsável por capturar e tratar exceções de forma global na API.
 * <p>
 * Utiliza {@link RestControllerAdvice} para interceptar exceções lançadas
 * pelos controllers e retornar respostas padronizadas do tipo {@link ErrorResponse}.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Constrói uma resposta de erro padronizada para as exceções lançadas.
     *
     * @param status  status HTTP da resposta.
     * @param message mensagem de erro detalhada.
     * @return ResponseEntity contendo {@link ErrorResponse} com dados do erro.
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }

    /**
     * Trata exceções genéricas inesperadas.
     *
     * @param e exceção genérica {@link Exception}.
     * @return Resposta HTTP 500 (INTERNAL SERVER ERROR) com mensagem de erro.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado: " + e.getMessage());
    }

    /**
     * Trata exceções de regra de negócio.
     *
     * @param e exceção do tipo {@link BusinessException}.
     * @return Resposta HTTP 400 (BAD REQUEST) com mensagem de erro.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * Trata exceção: jogo não encontrado.
     *
     * @param e exceção do tipo {@link GameNotFoundException}.
     * @return Resposta HTTP 404 (NOT FOUND) com mensagem de erro.
     */
    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGameNotFound(GameNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    /**
     * Trata exceção: usuário não encontrado.
     *
     * @param e exceção do tipo {@link UserNotFoundException}.
     * @return Resposta HTTP 404 (NOT FOUND) com mensagem de erro.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    /**
     * Trata exceção: aluguel não encontrado.
     *
     * @param e exceção do tipo {@link RentalNotFoundException}.
     * @return Resposta HTTP 404 (NOT FOUND) com mensagem de erro.
     */
    @ExceptionHandler(RentalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRentalNotFound(RentalNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    /**
     * Trata exceção: jogo indisponível para aluguel.
     *
     * @param e exceção do tipo {@link GameIsNotAvailableException}.
     * @return Resposta HTTP 409 (CONFLICT) com mensagem de erro.
     */
    @ExceptionHandler(GameIsNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleGameIsNotAvailable(GameIsNotAvailableException e) {
        return buildErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    /**
     * Trata exceção: limite de aluguéis ativos por plano excedido.
     *
     * @param e exceção do tipo {@link PlanLimitExceededException}.
     * @return Resposta HTTP 422 (UNPROCESSABLE ENTITY) com mensagem de erro.
     */
    @ExceptionHandler(PlanLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handlePlanLimitExceeded(PlanLimitExceededException e) {
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }

    /**
     * Trata exceção: tentativa de devolução de aluguel já encerrado.
     *
     * @param e exceção do tipo {@link RentalAlreadyClosedException}.
     * @return Resposta HTTP 409 (CONFLICT) com com mensagem de erro.
     */
    @ExceptionHandler(RentalAlreadyClosedException.class)
    public ResponseEntity<ErrorResponse> handleRentalAlreadyClosed(RentalAlreadyClosedException e) {
        return buildErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    /**
     * Trata tentativas de cadastro de usuário com dados já existentes.
     *
     * @param e exceção do tipo {@link DataIntegrityViolationException}.
     * @return Resposta HTTP 409 (CONFLICT) com mensagem de duplicidade.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(DataIntegrityViolationException e) {
        String message = "Usuário já cadastrado";
        return buildErrorResponse(HttpStatus.CONFLICT, message);
    }

    /**
     * Trata exceções de validação de argumentos recebidos (bean validation).
     *
     * @param e exceção do tipo {@link MethodArgumentNotValidException}.
     * @return Resposta HTTP 400 (BAD REQUEST) com campos inválidos detalhados.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> "[" + error.getField() + "] : " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Trata incompatibilidade de tipo nos parâmetros da URL ou query.
     *
     * @param e exceção do tipo {@link MethodArgumentTypeMismatchException}.
     * @return Resposta HTTP 400 (BAD REQUEST) informando o tipo esperado.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String paramName = e.getName();
        String expectedType = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "o tipo correto";
        String message = "O parâmetro " + paramName + " é inválido. Esperado: " + expectedType;

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Trata exceções de leitura da requisição (JSON inválido, mal formatado, etc).
     *
     * @param e exceção do tipo {@link HttpMessageNotReadableException}.
     * @return Resposta HTTP 400 (BAD REQUEST) ou tratamento específico de formatação.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException invalidFormat) {
            return handleInvalidFormat(invalidFormat);
        }
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Erro de leitura dos dados enviados na requisição");
    }

    /**
     * Trata exceções de formatação no JSON, focando em enums inválidos.
     *
     * @param e exceção do tipo {@link InvalidFormatException}.
     * @return Resposta HTTP 400 (BAD REQUEST) com lista de valores aceitos ou mensagem genérica.
     */
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

    /**
     * Trata exceção: login com credenciais inválidas.
     *
     * @param e exceção do tipo {@link BadCredentialsException}.
     * @return Resposta HTTP 401 (UNAUTHORIZED) com mensagem de erro.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException e) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
    }

    /**
     * Trata exceção: erro ao gerar token JWT.
     *
     * @param e exceção do tipo {@link JwtGenerationException}.
     * @return Resposta HTTP 500 (INTERNAL SERVER ERROR) com mensagem de erro.
     */
    @ExceptionHandler(JwtGenerationException.class)
    public ResponseEntity<ErrorResponse> handleJwtGenerationException(JwtGenerationException e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * Trata exceção: token JWT expirado.
     *
     * @param e exceção do tipo {@link JwtTokenExpiredException}.
     * @return Resposta HTTP 401 (UNAUTHORIZED) com mensagem de erro.
     */
    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleExpiredToken(JwtTokenExpiredException e) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    /**
     * Trata exceção: assinatura do token JWT inválida.
     *
     * @param e exceção do tipo {@link JwtSignatureVerificationException}.
     * @return Resposta HTTP 401 (UNAUTHORIZED) com mensagem de erro.
     */
    @ExceptionHandler(JwtSignatureVerificationException.class)
    public ResponseEntity<ErrorResponse> handleSignatureVerification(JwtSignatureVerificationException e) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    /**
     * Trata exceção: algoritmo do token JWT inválido.
     *
     * @param e exceção do tipo {@link JwtAlgorithmMismatchException}.
     * @return Resposta HTTP 401 (UNAUTHORIZED) com mensagem de erro.
     */
    @ExceptionHandler(JwtAlgorithmMismatchException.class)
    public ResponseEntity<ErrorResponse> handleAlgorithmMismatch(JwtAlgorithmMismatchException e) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    /**
     * Trata exceção: falha na verificação do token JWT.
     *
     * @param e exceção do tipo {@link JwtVerificationException}.
     * @return Resposta HTTP 401 (UNAUTHORIZED) com mensagem de erro.
     */
    @ExceptionHandler(JwtVerificationException.class)
    public ResponseEntity<ErrorResponse> handleJwtVerification(JwtVerificationException e) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }
}