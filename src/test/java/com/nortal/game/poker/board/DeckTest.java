package com.nortal.game.poker.board;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeckTest {

  @Test
  public void createDeck() throws Exception {
    Deck deck = new Deck();
    assertEquals(52, deck.cards.size());
  }

  @Test
  public void getNextCard() throws Exception {
    Deck deck = new Deck();
    deck.getNextCard();
    assertEquals(1, deck.cardsShared);
    deck.getNextCard();
    assertEquals(2, deck.cardsShared);
  }

  @Test
  public void collectCards() throws Exception {
    Deck deck = new Deck();
    deck.cardsShared = 10;
    deck.collectCards();
    assertEquals(0, deck.cardsShared);
  }
}