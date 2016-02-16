package com.nortal.game.poker.hand;

import com.nortal.game.poker.card.Card;
import com.nortal.game.poker.card.Rank;
import com.nortal.game.poker.card.Suit;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nortal.game.poker.card.Rank.ACE;
import static com.nortal.game.poker.card.Rank.FIVE;
import static com.nortal.game.poker.card.Rank.TWO;

public class StandardPokerHandEvaluator implements HandEvaluator {
  public static final long HIGH_CARD_BASE = 0;
  public static final long ONE_PAIR_BASE = HIGH_CARD_BASE + 73728;
  public static final long TWO_PAIRS_BASE = ONE_PAIR_BASE + 1414336;
  public static final long THREE_OF_A_KIND_BASE = TWO_PAIRS_BASE + 14134096;
  public static final long STRAIGHT_BASE = THREE_OF_A_KIND_BASE + 1416384;
  public static final long FLUSH_BASE = STRAIGHT_BASE + 14;
  public static final long FULL_HOUSE_BASE = FLUSH_BASE + 31744;
  public static final long FOUR_OF_A_KIND_BASE = FULL_HOUSE_BASE + 1413;
  public static final long STRAIGHT_FLUSH_BASE = FOUR_OF_A_KIND_BASE + 148192;

  @Override
  public long evaluate(Hand hand) {
    if (hand.size() != 5) return 0;
    long evaluationValue = 0;

    evaluationValue = getNewEvaluationValue(evaluationValue, getHighCardValue(hand));
    evaluationValue = getNewEvaluationValue(evaluationValue, getOnePairValue(hand));
    evaluationValue = getNewEvaluationValue(evaluationValue, getTwoPairsValue(hand));
    evaluationValue = getNewEvaluationValue(evaluationValue, getThreeOfAKindValue(hand));
    evaluationValue = getNewEvaluationValue(evaluationValue, getStraightValue(hand));
    evaluationValue = getNewEvaluationValue(evaluationValue, getFlushValue(hand));
    evaluationValue = getNewEvaluationValue(evaluationValue, getFullHouseValue(hand));
    evaluationValue = getNewEvaluationValue(evaluationValue, getFourOfAKindValue(hand));
    evaluationValue = getNewEvaluationValue(evaluationValue, getStraightFlushValue(hand));

    return evaluationValue;
  }

  protected long getStraightFlushValue(Hand hand) {
    long value = calculateStraightValue(hand, true);
    return value == 0 ? 0 : STRAIGHT_FLUSH_BASE + value;
  }

  protected long getFourOfAKindValue(Hand hand) {
    List<Rank> ranksInFour = getRanksInGroupOfSize(hand, 4);
    if (ranksInFour.size() != 1) return 0;

    long value = ranksInFour.get(0).getRankValue() * 10000;
    value += calculateKickersValue(hand, ranksInFour);
    return FOUR_OF_A_KIND_BASE + value;
  }

  protected long getFullHouseValue(Hand hand) {
    List<Rank> ranksInTriple = getRanksInGroupOfSize(hand, 3);
    List<Rank> ranksInPair = getRanksInGroupOfSize(hand, 2);
    if (ranksInTriple.size() != 1 || ranksInPair.size() != 1) return 0;
    return FULL_HOUSE_BASE + (ranksInTriple.get(0).getRankValue() * 100) + ranksInPair.get(0).getRankValue();
  }

  protected long getFlushValue(Hand hand) {
    Suit suit = hand.get(0).getSuit();
    long value = 0;
    for (Card card : hand) {
      if (!card.getSuit().equals(suit)) return 0;
      value += Math.pow(2, card.getRank().getRankValue());
    }
    return FLUSH_BASE + value;
  }

  protected long getStraightValue(Hand hand) {
    long value = calculateStraightValue(hand, false);
    return value == 0 ? 0 : STRAIGHT_BASE + value;
  }

  protected long getThreeOfAKindValue(Hand hand) {
    List<Rank> ranksInTriple = getRanksInGroupOfSize(hand, 3);
    if (ranksInTriple.size() != 1) return 0;

    long value = ranksInTriple.get(0).getRankValue() * 100000;
    value += calculateKickersValue(hand, ranksInTriple);
    return value + THREE_OF_A_KIND_BASE;
  }

  protected long getTwoPairsValue(Hand hand) {
    List<Rank> ranksInPair = getRanksInGroupOfSize(hand, 2);
    if (ranksInPair.size() != 2) return 0;
    Collections.sort(ranksInPair);
    Collections.reverse(ranksInPair);

    long value = ranksInPair.get(0).getRankValue() * 1000000;
    value += ranksInPair.get(1).getRankValue() * 10000;
    value += calculateKickersValue(hand, ranksInPair);
    return value + TWO_PAIRS_BASE;
  }

  protected long getOnePairValue(Hand hand) {
    List<Rank> ranksInPair = getRanksInGroupOfSize(hand, 2);
    if (ranksInPair.size() != 1) return 0;

    long value = ranksInPair.get(0).getRankValue() * 100000;
    value += calculateKickersValue(hand, ranksInPair);
    return value + ONE_PAIR_BASE;
  }

  protected long calculateKickersValue(Hand hand, List<Rank> ranksInGroup) {
    return (long)hand.stream().filter(card -> !ranksInGroup.contains(card.getRank()))
        .mapToDouble(card -> Math.pow(2, card.getRank().getRankValue()))
        .sum();
  }

  protected boolean cardsDoNotHaveConsecutiveRanks(Card currentCard, Card nextCard) {
    return currentCard.getRank().getRankValue() != nextCard.getRank().getRankValue() - 1;
  }

  protected boolean isFiveHighStraight(Card firstCard, Card lastButOneCard, Card lastCard) {
    return firstCard.getRank().equals(TWO)
        && lastButOneCard.getRank().equals(FIVE)
        && lastCard.getRank().equals(ACE);
  }

  protected long getHighCardValue(Hand hand) {
    return calculateKickersValue(hand, Collections.<Rank>emptyList());
  }

  protected Map<Rank, List<Card>> groupCardsByRank(Hand hand) {
    return hand.stream().collect(Collectors.groupingBy(Card::getRank));
  }

  protected long getNewEvaluationValue(long evaluationValue, long currentCategoryValue) {
    return currentCategoryValue > 0 ? currentCategoryValue : evaluationValue;
  }

  protected List<Rank> getRanksInGroupOfSize(Hand hand, int groupSize) {
    Map<Rank, List<Card>> cardsGroupedByRank = groupCardsByRank(hand);
    List<Rank> ranks = cardsGroupedByRank.keySet().stream()
        .filter(rank -> cardsGroupedByRank.get(rank).size() == groupSize)
        .collect(Collectors.toList());
    return ranks;
  }

  protected long calculateStraightValue(Hand hand, boolean isFlush) {
    Collections.sort(hand);
    for (int i = 0; i < hand.size()-1; i++) {
      Card currentCard = hand.get(i);
      Card nextCard = hand.get(i + 1);

      if (isFlush && !currentCard.getSuit().equals(nextCard.getSuit())) break;
      boolean isComparingLastCardInHand = i == hand.size() - 2;
      if (cardsDoNotHaveConsecutiveRanks(currentCard, nextCard)) {
        if (isFiveHighStraight(hand.get(0), currentCard, nextCard) && isComparingLastCardInHand) {
          return FIVE.getRankValue();
        } else {
          break;
        }
      }
      if (isComparingLastCardInHand) return nextCard.getRank().getRankValue();
    }
    return 0;
  }
}
