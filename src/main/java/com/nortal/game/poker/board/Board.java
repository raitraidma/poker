package com.nortal.game.poker.board;

import com.nortal.game.poker.hand.Hand;
import com.nortal.game.poker.hand.HandEvaluator;
import com.nortal.game.poker.player.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Board {
  public static final int MAX_PLAYERS_COUNT = 5;
  public static final int CARDS_PER_PLAYER = 5;
  private HandEvaluator handEvaluator;
  private Deck deck;

  protected Map<Player, Integer> chipsOnBoard = new HashMap<>();
  protected Map<Player, Hand> hands = new HashMap<>();

  public Board(Deck deck, HandEvaluator handEvaluator) {
    this.deck = deck;
    this.handEvaluator = handEvaluator;
  }

  public Board addPlayer(Player player) {
    if (chipsOnBoard.size() + 1 > MAX_PLAYERS_COUNT) throw new RuntimeException("Too many players");
    chipsOnBoard.put(player, 0);
    hands.put(player, new Hand());
    return this;
  }

  public boolean everybodyHasChips() {
    for (Player player : chipsOnBoard.keySet()) {
      if (!player.hasChips()) return false;
    }
    return true;
  }

  public void deal() {
    collectCardsFromPlayers();
    shuffleCards();
    dealCards();
    takeChipsFromEveryPlayer(1);
  }

  protected void shuffleCards() {
    deck.shuffle();
  }

  protected void collectCardsFromPlayers() {
    for (Player player : hands.keySet()) {
      hands.get(player).clear();
    }
    deck.collectCards();
  }

  protected void dealCards() {
    for (int card = 0; card < CARDS_PER_PLAYER; card++) {
      for (Player player : hands.keySet()) {
        hands.get(player).add(deck.getNextCard());
      }
    }
  }

  protected void takeChipsFromEveryPlayer(int chipCount) {
    for (Player player : chipsOnBoard.keySet()) {
      player.takeChips(chipCount);
      chipsOnBoard.put(player, chipsOnBoard.get(player) + chipCount);
    }
  }

  public Hand getPlayerHand(Player player) {
    return hands.get(player);
  }

  public int getMaxPossibleBid() {
    int maxPossibleBid = Integer.MAX_VALUE;
    for (Player player : chipsOnBoard.keySet()) {
      if (player.chipCount() < maxPossibleBid) {
        maxPossibleBid = player.chipCount();
      }
    }
    return maxPossibleBid;
  }

  public void raiseBid(int extraBid) {
    if (getMaxPossibleBid() < extraBid) throw new RuntimeException("Extra bid is too big: " + extraBid);
    takeChipsFromEveryPlayer(extraBid);
  }

  public List<Player> findPlayersWithBestHand() {
    Map<Player, Long> evaluations = getPlayersHandEvaluations();
    Long maxValue = evaluations.values().stream().max(Comparator.<Long>naturalOrder()).get();
    return evaluations.keySet().stream()
        .filter(player -> evaluations.get(player).equals(maxValue))
        .collect(Collectors.toList());
  }

  protected Map<Player, Long> getPlayersHandEvaluations() {
    Map<Player, Long> evaluations = new HashMap<>();
    for (Player player : hands.keySet()) {
      evaluations.put(player, handEvaluator.evaluate(hands.get(player)));
    }
    return evaluations;
  }

  public void giveChipsOnBoardToPlayer(Player newChipOwner) {
    for (Player player : chipsOnBoard.keySet()) {
      newChipOwner.addChips(chipsOnBoard.get(player));
      chipsOnBoard.put(player, 0);
    }
  }

  public void giveChipsBackToPlayers() {
    for (Player player : chipsOnBoard.keySet()) {
      player.addChips(chipsOnBoard.get(player));
      chipsOnBoard.put(player, 0);
    }
  }
}
