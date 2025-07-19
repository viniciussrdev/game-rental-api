package dev.viniciussr.gamerental.exception.game;

import dev.viniciussr.gamerental.exception.BusinessException;

// Exceção: Game Não Encontrado
public class GameNotFoundException extends BusinessException {
  public GameNotFoundException(String message) {
    super(message);
  }
}