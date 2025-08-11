package dev.viniciussr.gamerental.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

// Representa o modelo padrão para resposta de erro da API
public record ErrorResponse(

        int status, // Código HTTP do erro

        String error, // Descrição resumida do tipo de erro

        String message, // Mensagem detalhada sobre o erro ocorrido

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp // Data e hora em que o erro foi gerado
) {}