package com.nortal.game.poker.board;

import com.nortal.game.poker.card.Card;
import com.nortal.game.poker.card.Rank;
import com.nortal.game.poker.card.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
  public static final int CARDS_IN_DECK = 52;
  protected List<Card> cards = new ArrayList<>(CARDS_IN_DECK);
  protected int cardsShared = 0;

  public Deck() {
    createDeck();
  }

  private void createDeck() {
    for (Suit suit : Suit.values()) {
      for (Rank rank : Rank.values()) {
        cards.add(new Card(rank, suit));
      }
    }
  }

  public void shuffle() {
    Collections.shuffle(cards);
  }

  public Card getNextCard() {
    cardsShared++;
    return cards.get(cardsShared-1);
  }

  public void collectCards() {
    cardsShared = 0;
  }
}
