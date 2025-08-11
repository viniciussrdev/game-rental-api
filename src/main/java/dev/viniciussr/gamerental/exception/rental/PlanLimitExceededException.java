package dev.viniciussr.gamerental.exception.rental;

import dev.viniciussr.gamerental.exception.BusinessException;
import dev.viniciussr.gamerental.model.User;

// Exceção lançada quando um usuário ultrapassa o limite de aluguéis ativos permitido pelo seu plano
public class PlanLimitExceededException extends BusinessException {
    public PlanLimitExceededException(User user) {
        super("Limite de aluguéis ativos excedido para o usuário " + user.getName() + " no plano " + user.getPlan());
    }
}