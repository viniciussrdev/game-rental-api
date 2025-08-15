package dev.viniciussr.gamerental.exception.game;

import dev.viniciussr.gamerental.exception.BusinessException;
import dev.viniciussr.gamerental.model.Game;

/**
 * Exceção lançada quando um jogo não está disponível para aluguel.
 */
public class GameIsNotAvailableException extends BusinessException {
  public GameIsNotAvailableException(Game game ) {
    super(game.getTitle() + " está indisponível para aluguel. Quantidade: " + game.getQuantity());
  }
}