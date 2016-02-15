package com.nortal.game;

import com.nortal.game.poker.board.Board;
import com.nortal.game.poker.board.Deck;
import com.nortal.game.poker.hand.HandEvaluator;
import com.nortal.game.poker.hand.StandardPokerHandEvaluator;
import com.nortal.game.poker.player.Computer;
import com.nortal.game.poker.player.Human;
import com.nortal.game.poker.player.Player;
import com.nortal.game.poker.ui.ConsoleUI;
import com.nortal.game.poker.ui.UI;

import java.util.List;

public class Poker {
  private Deck deck;
  private HandEvaluator handEvaluator;
  private Board board;
  private Player human;
  private Player computer;
  private UI userInterface;

  public static void main(String[] args) {
    Poker poker = new Poker();
    poker.init();
    poker.play();
  }

  private void init() {
    deck = new Deck();
    handEvaluator = new StandardPokerHandEvaluator();

    board = new Board(deck, handEvaluator);
    userInterface = new ConsoleUI();

    human = new Human("Human");
    human.setChips(20);
    computer = new Computer("Computer");
    computer.setChips(20);

    board.addPlayer(human)
        .addPlayer(computer);
  }

  private void play() {
    while(board.everybodyHasChips()) {
      userInterface.newRound();

      board.deal();
      userInterface.showPlayerCards(human, board.getPlayerHand(human));

      int extraBid = userInterface.getExtraBid(board.getMaxPossibleBid());
      board.raiseBid(extraBid);

      userInterface.showPlayerCards(human, board.getPlayerHand(human));
      userInterface.showPlayerCards(computer, board.getPlayerHand(computer));

      List<Player> bestHandPlayers = board.findPlayersWithBestHand();
      if (bestHandPlayers.size() == 1) {
        board.giveChipsOnBoardToPlayer(bestHandPlayers.get(0));
        userInterface.announceWinner(bestHandPlayers.get(0));
      } else {
        board.giveChipsBackToPlayers();
        userInterface.announceChipsReturn();
      }
    }
    userInterface.gameOver();
  }
}
