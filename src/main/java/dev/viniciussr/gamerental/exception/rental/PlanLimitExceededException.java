package dev.viniciussr.gamerental.exception.rental;

import dev.viniciussr.gamerental.exception.BusinessException;
import dev.viniciussr.gamerental.model.User;

// Exceção: Limite do Plano Excedido
public class PlanLimitExceededException extends BusinessException {
    public PlanLimitExceededException(User user) {
        super("Limite de aluguéis ativos excedido para o usuário " + user.getName() + " no plano " + user.getPlan());
    }
}