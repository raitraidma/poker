package com.nortal.game.poker.hand;

import com.nortal.game.poker.card.Card;
import com.nortal.game.poker.card.Rank;
import com.nortal.game.poker.card.Suit;
import org.junit.Test;

import static com.nortal.game.poker.card.Rank.*;
import static com.nortal.game.poker.card.Suit.*;
import static com.nortal.game.poker.hand.StandardPokerHandEvaluator.*;
import static org.junit.Assert.assertEquals;

public class StandardPokerHandEvaluatorTest {

  @Test
  public void getNewEvaluationValue() throws Exception {
    assertEquals(10, new StandardPokerHandEvaluator().getNewEvaluationValue(10, 0));
    assertEquals(20, new StandardPokerHandEvaluator().getNewEvaluationValue(10, 20));
  }

  @Test
  public void getHighCardValue() throws Exception {
    Hand hand = createHand(card(ACE, SPADE), card(KING, SPADE), card(QUEEN, SPADE), card(JACK, SPADE), card(TEN, SPADE));
    assertEquals(31744, new StandardPokerHandEvaluator().getHighCardValue(hand));
  }

  @Test
  public void getOnePairValue() throws Exception {
    Hand hand = createHand(card(ACE, SPADE), card(ACE, SPADE), card(KING, SPADE), card(QUEEN, SPADE), card(JACK, SPADE));
    assertEquals(1414336 + ONE_PAIR_BASE, new StandardPokerHandEvaluator().getOnePairValue(hand));
  }

  @Test
  public void getTwoPairsValue() throws Exception {
    Hand hand = createHand(card(ACE, SPADE), card(ACE, SPADE), card(KING, SPADE), card(KING, SPADE), card(QUEEN, SPADE));
    assertEquals(141312 + TWO_PAIRS_BASE, new StandardPokerHandEvaluator().getTwoPairsValue(hand));
  }

  @Test
  public void getThreeOfAKindValue() throws Exception {
    Hand hand = createHand(card(ACE, SPADE), card(ACE, SPADE), card(ACE, SPADE), card(KING, SPADE), card(KING, SPADE));
    assertEquals(1416384 + THREE_OF_A_KIND_BASE, new StandardPokerHandEvaluator().getThreeOfAKindValue(hand));
  }

  @Test
  public void getStraightValue() throws Exception {
    Hand hand = createHand(card(ACE, SPADE), card(KING, SPADE), card(QUEEN, SPADE), card(JACK, SPADE), card(TEN, SPADE));
    assertEquals(14 + STRAIGHT_BASE, new StandardPokerHandEvaluator().getStraightValue(hand));
  }

  @Test
  public void getStraight5HighValue() throws Exception {
    Hand hand = createHand(card(FIVE, SPADE), card(FOUR, SPADE), card(THREE, SPADE), card(TWO, SPADE), card(ACE, SPADE));
    assertEquals(5 + STRAIGHT_BASE, new StandardPokerHandEvaluator().getStraightValue(hand));
  }

  @Test
  public void getStraightValueWrong() throws Exception {
    Hand hand = createHand(card(FIVE, SPADE), card(SEVEN, SPADE), card(SIX, SPADE), card(TWO, SPADE), card(ACE, SPADE));
    assertEquals(0, new StandardPokerHandEvaluator().getStraightValue(hand));
  }

  @Test
  public void getFlushValue() throws Exception {
    Hand hand = createHand(card(ACE, SPADE), card(KING, SPADE), card(QUEEN, SPADE), card(JACK, SPADE), card(TEN, SPADE));
    assertEquals(31744 + FLUSH_BASE, new StandardPokerHandEvaluator().getFlushValue(hand));
  }

  @Test
  public void getFlushValueWithDifferentSuit() throws Exception {
    Hand hand = createHand(card(ACE, SPADE), card(KING, SPADE), card(QUEEN, SPADE), card(JACK, SPADE), card(TEN, DIAMOND));
    assertEquals(0, new StandardPokerHandEvaluator().getFlushValue(hand));
  }

  @Test
  public void getFullHouseValue() throws Exception {
    Hand hand = createHand(card(ACE, SPADE), card(ACE, SPADE), card(ACE, SPADE), card(KING, SPADE), card(KING, DIAMOND));
    assertEquals(1413 + FULL_HOUSE_BASE, new StandardPokerHandEvaluator().getFullHouseValue(hand));
  }

  @Test
  public void getFourOfAKindValue() throws Exception {
    Hand hand = createHand(card(ACE, SPADE), card(ACE, SPADE), card(ACE, SPADE), card(ACE, SPADE), card(KING, SPADE));
    assertEquals(1413 + FOUR_OF_A_KIND_BASE, new StandardPokerHandEvaluator().getFourOfAKindValue(hand));
  }

  @Test
  public void getStraightFlushValue() throws Exception {
    Hand hand = createHand(card(ACE, SPADE), card(KING, SPADE), card(QUEEN, SPADE), card(JACK, SPADE), card(TEN, SPADE));
    assertEquals(14 + STRAIGHT_FLUSH_BASE, new StandardPokerHandEvaluator().getStraightFlushValue(hand));
  }

  @Test
  public void getStraightFlush5HighValue() throws Exception {
    Hand hand = createHand(card(FIVE, SPADE), card(FOUR, SPADE), card(THREE, SPADE), card(TWO, SPADE), card(ACE, SPADE));
    assertEquals(5 + STRAIGHT_FLUSH_BASE, new StandardPokerHandEvaluator().getStraightFlushValue(hand));
  }

  @Test
  public void getStraightFlush5HighValueWithWrongSuit() throws Exception {
    Hand hand = createHand(card(FIVE, SPADE), card(FOUR, SPADE), card(THREE, SPADE), card(TWO, SPADE), card(ACE, DIAMOND));
    assertEquals(0, new StandardPokerHandEvaluator().getStraightFlushValue(hand));
  }

  private Card card(Rank rank, Suit suit) {
    return new Card(rank, suit);
  }

  private Hand createHand(Card card1, Card card2, Card card3, Card card4, Card card5) {
    Hand hand = new Hand();
    hand.add(card1);
    hand.add(card2);
    hand.add(card3);
    hand.add(card4);
    hand.add(card5);
    return hand;
  }
}