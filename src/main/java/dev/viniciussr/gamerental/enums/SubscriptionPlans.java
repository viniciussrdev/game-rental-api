package dev.viniciussr.gamerental.enums;

/**
 * Representa os planos de assinatura disponíveis para os usuários do sistema.
 * <p>
 * Cada plano define a quantidade máxima de jogos que o usuário pode alugar
 * simultaneamente.
 * </p>
 */
public enum SubscriptionPlans {

    /**
     * Permite até 1 (um) aluguel por vez.
     */
    NOOB,

    /**
     * Permite até 3 (três) aluguéis por vez.
     */
    PRO,

    /**
     * Permite até 5 (cinco) aluguéis por vez.
     */
    LEGEND
}