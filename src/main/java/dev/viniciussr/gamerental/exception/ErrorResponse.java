package dev.viniciussr.gamerental.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

// Mensagem de Erro padronizada para respostas HTTP
public record ErrorResponse(

        int status,

        String error,

        String message,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp
) {}