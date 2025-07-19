package dev.viniciussr.gamerental.exception.game;

import dev.viniciussr.gamerental.exception.BusinessException;
import dev.viniciussr.gamerental.model.Game;

// Exceção: Game Indisponível
public class GameIsNotAvailableException extends BusinessException {
  public GameIsNotAvailableException(Game game ) {
    super(game.getTitle() + " está indisponível para aluguel. Quantidade: " + game.getQuantity());
  }
}