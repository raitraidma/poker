package com.nortal.game;

import com.nortal.game.poker.board.Board;
import com.nortal.game.poker.board.Deck;
import com.nortal.game.poker.hand.StandardPokerHandEvaluator;
import com.nortal.game.poker.player.Computer;
import com.nortal.game.poker.player.Human;
import com.nortal.game.poker.player.Player;
import com.nortal.game.poker.ui.ConsoleUI;
import com.nortal.game.poker.ui.UI;

import java.util.List;

public class Poker {
  protected Board board;
  protected Player human;
  protected Player computer;
  protected UI userInterface;

  public static void main(String[] args) {
    Poker poker = new Poker();
    poker.init();
    poker.play();
  }

  private void init() {
    board = new Board(new Deck(), new StandardPokerHandEvaluator());
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
      raiseBid();
      openCards();
      giveChipsToWinnerOrReturnChips();
    }
    userInterface.gameOver();
  }

  protected void raiseBid() {
    int extraBid = userInterface.getExtraBid(board.getMaxPossibleBid());
    board.raiseBid(extraBid);
  }

  protected void openCards() {
    userInterface.showPlayerCards(human, board.getPlayerHand(human));
    userInterface.showPlayerCards(computer, board.getPlayerHand(computer));
  }

  protected void giveChipsToWinnerOrReturnChips() {
    List<Player> playersWithBestHand = board.findPlayersWithBestHand();
    if (playersWithBestHand.size() == 1) {
      board.giveChipsOnBoardToPlayer(playersWithBestHand.get(0));
      userInterface.announceWinner(playersWithBestHand.get(0));
    } else {
      board.giveChipsBackToPlayers();
      userInterface.announceChipsReturn();
    }
  }
}
