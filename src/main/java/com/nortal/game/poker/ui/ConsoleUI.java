package com.nortal.game.poker.ui;

import com.nortal.game.poker.hand.Hand;
import com.nortal.game.poker.player.Player;

import java.util.Scanner;

public class ConsoleUI extends UI {
  protected Scanner consoleInputReader;
  public ConsoleUI() {
    consoleInputReader = new Scanner(System.in);
  }

  @Override
  public void showPlayerCards(Player player, Hand hand) {
    displayToUser("Cards of " + player.getName());
    displayToUser(hand.toString());
  }

  @Override
  public int getExtraBid(int maxPossibleBid) {
    displayToUser("Raise your bid (0 - " + maxPossibleBid + "):");
    int extraBid = 0;
    while (consoleInputReader.hasNextLine()) {
      String nextLine = consoleInputReader.nextLine();
      try {
        extraBid = Integer.parseInt(nextLine);
        if (extraBid <= maxPossibleBid) {
          return extraBid;
        }
        displayToUser("Bid must be between 0 and " + maxPossibleBid);
      } catch (NumberFormatException e) {
        displayToUser("Bid must be a number");
      }
    }
    return 0;
  }

  @Override
  public void announceWinner(Player winner) {
    displayToUser("The winner is: " + winner.getName());
  }

  @Override
  public void announceChipsReturn() {
    displayToUser("All chips were returned");
  }

  @Override
  public void gameOver() {
    displayToUser("Game over!");
  }

  @Override
  public void newRound() {
    displayToUser("- - - - -");
  }

  protected void displayToUser(String text) {
    System.out.println(text);
  }
}
