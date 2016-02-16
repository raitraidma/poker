package com.nortal.game.poker.hand;

import com.nortal.game.poker.card.Card;
import com.nortal.game.poker.card.Rank;
import com.nortal.game.poker.card.Suit;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nortal.game.poker.card.Rank.*;
import static com.nortal.game.poker.card.Suit.*;
import static com.nortal.game.poker.hand.StandardPokerHandEvaluator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class StandardPokerHandEvaluatorTest {

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
    assertEquals(14134096 + TWO_PAIRS_BASE, new StandardPokerHandEvaluator().getTwoPairsValue(hand));
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
    assertEquals(148192 + FOUR_OF_A_KIND_BASE, new StandardPokerHandEvaluator().getFourOfAKindValue(hand));
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

  @Test
  public void getNewEvaluationValue() throws Exception {
    assertEquals(10, new StandardPokerHandEvaluator().getNewEvaluationValue(10, 0));
    assertEquals(20, new StandardPokerHandEvaluator().getNewEvaluationValue(10, 20));
  }

  @Test
  public void calculateKickersValue() throws Exception {
    Hand hand = createHand(card(ACE, SPADE), card(KING, SPADE), card(QUEEN, SPADE), card(JACK, SPADE), card(TEN, SPADE));
    assertEquals(31744, new StandardPokerHandEvaluator().calculateKickersValue(hand, new ArrayList<>()));
  }

  @Test
  public void getRanksInGroupOfSize() throws Exception {
    Hand hand = createHand(card(FIVE, SPADE), card(FIVE, DIAMOND), card(THREE, SPADE), card(THREE, DIAMOND), card(ACE, DIAMOND));
    List<Rank> ranksInGroupOfSize2 = new StandardPokerHandEvaluator().getRanksInGroupOfSize(hand, 2);
    assertEquals(2, ranksInGroupOfSize2.size());
    assertTrue(ranksInGroupOfSize2.contains(FIVE));
    assertTrue(ranksInGroupOfSize2.contains(THREE));
  }

  @Test
  public void groupCardsByRank() throws Exception {
    Hand hand = createHand(card(FIVE, SPADE), card(FIVE, DIAMOND), card(THREE, SPADE), card(THREE, DIAMOND), card(ACE, DIAMOND));
    Map<Rank, List<Card>> cardsByRank = new StandardPokerHandEvaluator().groupCardsByRank(hand);
    assertEquals(3, cardsByRank.size());
    assertEquals(2, cardsByRank.get(FIVE).size());
    assertEquals(2, cardsByRank.get(THREE).size());
    assertEquals(1, cardsByRank.get(ACE).size());
  }

  @Test
  public void cardsDoNotHaveConsecutiveRanks() throws Exception {
    StandardPokerHandEvaluator handEvaluator = new StandardPokerHandEvaluator();
    assertTrue(handEvaluator.cardsDoNotHaveConsecutiveRanks(card(FIVE, SPADE), card(SEVEN, SPADE)));
    assertTrue(handEvaluator.cardsDoNotHaveConsecutiveRanks(card(SIX, SPADE), card(FIVE, SPADE)));
    assertFalse(handEvaluator.cardsDoNotHaveConsecutiveRanks(card(SIX, SPADE), card(SEVEN, SPADE)));
  }

  @Test
  public void isFiveHighStraight() throws Exception {
    StandardPokerHandEvaluator handEvaluator = new StandardPokerHandEvaluator();
    assertTrue(handEvaluator.isFiveHighStraight(card(TWO, SPADE), card(FIVE, SPADE), card(ACE, SPADE)));
    assertFalse(handEvaluator.isFiveHighStraight(card(ACE, SPADE), card(FIVE, SPADE), card(TWO, SPADE)));
  }

  @Test
  public void onePairIsBetterThanHighCard() throws Exception {
    StandardPokerHandEvaluator handEvaluator = new StandardPokerHandEvaluator();
    Hand highCardHigh = createHand(card(KING, DIAMOND), card(ACE, DIAMOND), card(ACE, HEART), card(ACE, CLUB), card(ACE, SPADE));
    Hand onePairLow = createHand(card(TWO, SPADE), card(TWO, DIAMOND), card(THREE, HEART), card(THREE, SPADE), card(THREE, CLUB));
    assertTrue(handEvaluator.getHighCardValue(highCardHigh) < handEvaluator.getOnePairValue(onePairLow));
  }

  @Test
  public void twoPairsIsBetterThanOnePair() throws Exception {
    StandardPokerHandEvaluator handEvaluator = new StandardPokerHandEvaluator();
    Hand onePairHigh = createHand(card(ACE, SPADE), card(ACE, DIAMOND), card(KING, HEART), card(KING, SPADE), card(KING, CLUB));
    Hand twoPairsLow = createHand(card(TWO, DIAMOND), card(TWO, DIAMOND), card(THREE, HEART), card(THREE, CLUB), card(FOUR, SPADE));
    assertTrue(handEvaluator.getOnePairValue(onePairHigh) < handEvaluator.getTwoPairsValue(twoPairsLow));
  }

  @Test
  public void threeOfAKindIsBetterThanTwoPairs() throws Exception {
    StandardPokerHandEvaluator handEvaluator = new StandardPokerHandEvaluator();
    Hand twoPairsHigh = createHand(card(ACE, DIAMOND), card(ACE, DIAMOND), card(KING, HEART), card(KING, CLUB), card(QUEEN, SPADE));
    Hand threeOfAKindLow = createHand(card(TWO, SPADE), card(TWO, DIAMOND), card(TWO, HEART), card(THREE, SPADE), card(THREE, CLUB));
    assertTrue(handEvaluator.getTwoPairsValue(twoPairsHigh) < handEvaluator.getThreeOfAKindValue(threeOfAKindLow));
  }

  @Test
  public void straightIsBetterThanThreeOfAKind() throws Exception {
    StandardPokerHandEvaluator handEvaluator = new StandardPokerHandEvaluator();
    Hand threeOfAKindHigh = createHand(card(ACE, SPADE), card(ACE, DIAMOND), card(ACE, HEART), card(KING, SPADE), card(KING, CLUB));
    Hand straightLow = createHand(card(TWO, DIAMOND), card(THREE, DIAMOND), card(FOUR, HEART), card(FIVE, CLUB), card(ACE, SPADE));
    assertTrue(handEvaluator.getThreeOfAKindValue(threeOfAKindHigh) < handEvaluator.getStraightValue(straightLow));
  }

  @Test
  public void flushIsBetterThanStraight() throws Exception {
    StandardPokerHandEvaluator handEvaluator = new StandardPokerHandEvaluator();
    Hand straightHigh = createHand(card(ACE, DIAMOND), card(KING, DIAMOND), card(QUEEN, HEART), card(JACK, CLUB), card(TEN, SPADE));
    Hand flushLow = createHand(card(TWO, SPADE), card(THREE, SPADE), card(FOUR, SPADE), card(FIVE, SPADE), card(SIX, SPADE));
    assertTrue(handEvaluator.getStraightValue(straightHigh) < handEvaluator.getFlushValue(flushLow));
  }

  @Test
  public void fullHouseIsBetterThanFlush() throws Exception {
    StandardPokerHandEvaluator handEvaluator = new StandardPokerHandEvaluator();
    Hand flushHigh = createHand(card(ACE, SPADE), card(KING, SPADE), card(QUEEN, SPADE), card(JACK, SPADE), card(TEN, SPADE));
    Hand fullHouseLow = createHand(card(TWO, DIAMOND), card(TWO, DIAMOND), card(TWO, HEART), card(THREE, CLUB), card(THREE, SPADE));
    assertTrue(handEvaluator.getFlushValue(flushHigh) < handEvaluator.getFullHouseValue(fullHouseLow));
  }

  @Test
  public void fourOfAKindIsBetterThanFullHouse() throws Exception {
    StandardPokerHandEvaluator handEvaluator = new StandardPokerHandEvaluator();
    Hand fullHouseHigh = createHand(card(ACE, DIAMOND), card(ACE, DIAMOND), card(ACE, HEART), card(KING, CLUB), card(KING, SPADE));
    Hand fourOfAKindLow = createHand(card(TWO, SPADE), card(TWO, SPADE), card(TWO, SPADE), card(TWO, SPADE), card(THREE, SPADE));
    assertTrue(handEvaluator.getFullHouseValue(fullHouseHigh) < handEvaluator.getFourOfAKindValue(fourOfAKindLow));
  }

  @Test
  public void straightFlushIsBetterThanFourOfAKind() throws Exception {
    StandardPokerHandEvaluator handEvaluator = new StandardPokerHandEvaluator();
    Hand fourOfAKindHigh = createHand(card(ACE, SPADE), card(ACE, SPADE), card(ACE, SPADE), card(ACE, SPADE), card(KING, SPADE));
    Hand straightFlushLow = createHand(card(TWO, SPADE), card(THREE, SPADE), card(FOUR, SPADE), card(FIVE, SPADE), card(ACE, SPADE));
    assertTrue(handEvaluator.getFourOfAKindValue(fourOfAKindHigh) < handEvaluator.getStraightFlushValue(straightFlushLow));
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