package com.nortal.game;

import com.nortal.game.poker.board.Board;
import com.nortal.game.poker.card.Card;
import com.nortal.game.poker.hand.Hand;
import com.nortal.game.poker.player.Computer;
import com.nortal.game.poker.player.Human;
import com.nortal.game.poker.player.Player;
import com.nortal.game.poker.ui.UI;
import org.junit.Test;

import static com.nortal.game.poker.card.Rank.ACE;
import static com.nortal.game.poker.card.Rank.KING;
import static com.nortal.game.poker.card.Suit.SPADE;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class PokerTest {
  @Test
  public void raiseBid() throws Exception {
    Poker poker = new Poker();

    poker.userInterface = mock(UI.class);
    doReturn(3).when(poker.userInterface).getExtraBid(anyInt());

    poker.board = mock(Board.class);
    doReturn(5).when(poker.board).getMaxPossibleBid();
    doNothing().when(poker.board).raiseBid(anyInt());

    poker.raiseBid();

    verify(poker.userInterface).getExtraBid(eq(5));
    verify(poker.board).raiseBid(eq(3));
  }

  @Test
  public void openCards() throws Exception {
    Poker poker = new Poker();
    poker.human = new Human("player-1");
    poker.computer = new Computer("player-2");

    Hand humanHand = new Hand() {{ add(new Card(ACE, SPADE)); }};
    Hand computerHand = new Hand() {{ add(new Card(KING, SPADE)); }};

    poker.userInterface = mock(UI.class);
    doNothing().when(poker.userInterface).showPlayerCards(any(Player.class), any(Hand.class));

    poker.board = mock(Board.class);
    doReturn(humanHand).when(poker.board).getPlayerHand(poker.human);
    doReturn(computerHand).when(poker.board).getPlayerHand(poker.computer);

    poker.openCards();

    verify(poker.userInterface).showPlayerCards(poker.human, humanHand);
    verify(poker.userInterface).showPlayerCards(poker.computer, computerHand);
  }

  @Test
  public void returnChips() throws Exception {
    Poker poker = new Poker();
    poker.board = mock(Board.class);
    doReturn(asList(new Human("player-1"), new Computer("palyer-2"))).when(poker.board).findPlayersWithBestHand();
    poker.userInterface = mock(UI.class);
    poker.giveChipsToWinnerOrReturnChips();

    verify(poker.board).giveChipsBackToPlayers();
    verify(poker.userInterface).announceChipsReturn();
  }

  @Test
  public void giveChipsToWinner() throws Exception {
    Poker poker = new Poker();
    Player player = new Human("player-1");
    poker.board = mock(Board.class);
    doReturn(asList(player)).when(poker.board).findPlayersWithBestHand();
    poker.userInterface = mock(UI.class);
    poker.giveChipsToWinnerOrReturnChips();

    verify(poker.board).giveChipsOnBoardToPlayer(player);
    verify(poker.userInterface).announceWinner(player);
  }
}