package dev.viniciussr.gamerental.exception.game;

import dev.viniciussr.gamerental.exception.BusinessException;

// Exceção lançada quando um jogo não é encontrado no banco de dados
public class GameNotFoundException extends BusinessException {
  public GameNotFoundException(String message) {
    super(message);
  }
}