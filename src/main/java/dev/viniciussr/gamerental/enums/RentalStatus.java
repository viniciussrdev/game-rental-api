package dev.viniciussr.gamerental.enums;

/**
 * Representa o status atual de um aluguel no sistema.
 * <p>
 * Os status possíveis indicam a situação do aluguel em relação à sua vigência,
 * devolução ou cancelamento.
 * </p>
 */
public enum RentalStatus {

    /**
     * O aluguel está ativo e dentro do prazo estabelecido.
     */
    ACTIVE,

    /**
     * O item foi devolvido dentro do prazo ou após atraso já regularizado.
     */
    RETURNED,

    /**
     * O aluguel ultrapassou o prazo de devolução e ainda não foi regularizado.
     */
    LATE,

    /**
     * O aluguel foi cancelado antes de sua conclusão.
     */
    CANCELLED
}