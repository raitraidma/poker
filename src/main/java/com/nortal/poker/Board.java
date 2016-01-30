package com.nortal.poker;

import com.nortal.poker.card.Card;
import com.nortal.poker.player.Player;

import java.util.HashMap;
import java.util.Map;

public class Board {
  private Map<Player, Integer> chips = new HashMap<>();
  private Map<Player, Card> cards = new HashMap<>();
  private GameState gameState;
}
