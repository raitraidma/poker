package com.nortal.game.poker.player;

public abstract class Player {
  protected String name;
  protected int chips;

  public Player(String name) {
    this.name = name;
  }

  public void setChips(int chips) {
    this.chips = chips;
  };

  public void addChips(int chips) {
    this.chips += chips;
  };

  public void takeChips(int chipsToTake) {
    if (chips < chipsToTake) throw new RuntimeException("User does not have that many chips");
    chips -= chipsToTake;
  }

  public boolean hasChips() {
    return chips > 0;
  }

  public int chipCount() {
    return chips;
  }

  public String getName() {
    return name;
  }
}
