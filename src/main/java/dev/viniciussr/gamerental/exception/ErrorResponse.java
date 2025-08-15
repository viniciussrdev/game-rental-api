package dev.viniciussr.gamerental.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Representa o modelo padrão para respostas de erro da API.
 *
 * @param status    código HTTP do erro
 * @param error     descrição resumida do tipo de erro
 * @param message   mensagem detalhada sobre o erro ocorrido
 * @param timestamp data e hora em que o erro foi gerado
 */
public record ErrorResponse(

        int status,

        String error,

        String message,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp
) {}