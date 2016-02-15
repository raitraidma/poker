package com.nortal.game.poker.card;

public class Card implements Comparable<Card> {
  private Rank rank;
  private Suit suit;

  public Card(Rank rank, Suit suit) {
    this.rank = rank;
    this.suit = suit;
  }

  @Override
  public String toString() {
    return rank + " " + suit;
  }

  public Rank getRank() {
    return rank;
  }

  public Suit getSuit() {
    return suit;
  }

  @Override
  public int compareTo(Card otherCard) {
    return (this.getRank().getRankValue() < otherCard.getRank().getRankValue()) ? -1 : 1;
  }
}
