package com.nortal.game.poker.card;

public enum Rank {
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5),
  SIX(6),
  SEVEN(7),
  EIGHT(8),
  NINE(9),
  TEN(10),
  JACK(11),
  QUEEN(12),
  KING(13),
  ACE(14);

  private int rankValue;

  Rank(int rankValue) {
    this.rankValue = rankValue;
  }

  public int getRankValue() {
    return rankValue;
  }

  @Override
  public String toString() {
    if (rankValue <= 10) return String.valueOf(rankValue);
    return name();
  }
}
