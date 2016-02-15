package com.nortal.game.poker.ui;

import com.nortal.game.poker.hand.Hand;
import com.nortal.game.poker.player.Player;

public abstract class UI {
  public abstract void showPlayerCards(Player human, Hand hand);

  public abstract int getExtraBid(int maxPossibleBid);

  public abstract void announceWinner(Player winner);

  public abstract void announceChipsReturn();

  public abstract void gameOver();

  public abstract void newRound();
}
