package com.nortal.game.poker.card;

import org.junit.Test;

import static com.nortal.game.poker.card.Rank.ACE;
import static com.nortal.game.poker.card.Suit.SPADE;
import static org.junit.Assert.assertEquals;

public class CardTest {

  @Test
  public void getRank() throws Exception {
    Card card = new Card(ACE, SPADE);
    assertEquals(ACE, card.getRank());
  }

  @Test
  public void getSuit() throws Exception {
    Card card = new Card(ACE, SPADE);
    assertEquals(SPADE, card.getSuit());
  }
}