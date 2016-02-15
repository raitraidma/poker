package com.nortal.game.poker.board;

import com.nortal.game.poker.card.Card;
import com.nortal.game.poker.hand.Hand;
import com.nortal.game.poker.hand.HandEvaluator;
import com.nortal.game.poker.player.Computer;
import com.nortal.game.poker.player.Human;
import com.nortal.game.poker.player.Player;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nortal.game.poker.card.Rank.ACE;
import static com.nortal.game.poker.card.Rank.KING;
import static com.nortal.game.poker.card.Suit.SPADE;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class BoardTest {
  @Test
  public void addPlayer() throws Exception {
    Board board = new Board(null, null);
    Player human = new Human("Human");
    Player computer = new Computer("Computer");
    board.addPlayer(human);
    board.addPlayer(computer);

    assertEquals(2, board.chipsOnBoard.size());
    assertEquals(new Integer(0), board.chipsOnBoard.get(human));
    assertEquals(new Integer(0), board.chipsOnBoard.get(computer));

    assertEquals(2, board.hands.size());
    assertEquals(0, board.hands.get(human).size());
    assertEquals(0, board.hands.get(computer).size());
  }

  @Test(expected = RuntimeException.class)
  public void addPlayerThrowsException() throws Exception {
    Board board = new Board(null, null);
    for (int i = 0; i <= Board.MAX_PLAYERS_COUNT; i++) {
      board.addPlayer(new Human("Player-" + i));
    }
  }

  @Test
  public void everybodyHasChips() throws Exception {
    Board board = new Board(null, null);
    Player human = new Human("Human");
    human.setChips(2);
    board.chipsOnBoard.put(human, 0);
    assertTrue(board.everybodyHasChips());
  }

  @Test
  public void playerDoesNotHasChips() throws Exception {
    Board board = new Board(null, null);
    Player human = new Human("Human");
    human.setChips(0);
    board.chipsOnBoard.put(human, 0);
    assertFalse(board.everybodyHasChips());
  }

  @Test
  public void deal() throws Exception {
    Board board = spy(new Board(null, null));
    doNothing().when(board).collectCardsFromPlayers();
    doNothing().when(board).shuffleCards();
    doNothing().when(board).dealCards();
    doNothing().when(board).takeChipsFromEveryPlayer(eq(1));

    board.deal();

    verify(board).collectCardsFromPlayers();
    verify(board).shuffleCards();
    verify(board).deal();
    verify(board).takeChipsFromEveryPlayer(eq(1));
  }

  @Test
  public void shuffleCards() throws Exception {
    Deck deck = mock(Deck.class);
    doNothing().when(deck).shuffle();
    new Board(deck, null).shuffleCards();
    verify(deck).shuffle();
  }

  @Test
  public void collectCardsFromPlayers() throws Exception {
    Deck deck = mock(Deck.class);
    doNothing().when(deck).shuffle();
    new Board(deck, null).shuffleCards();
    verify(deck).shuffle();
  }

  @Test
  public void dealCards() throws Exception {
    Deck deck = mock(Deck.class);
    doReturn(new Card(ACE, SPADE)).when(deck).getNextCard();
    Board board = new Board(deck, null);
    Player human = new Human("player-1");
    Player computer = new Computer("player-2");
    board.hands.put(human, new Hand());
    board.hands.put(computer, new Hand());
    board.dealCards();
    assertEquals(5, board.hands.get(human).size());
    assertEquals(5, board.hands.get(computer).size());
  }

  @Test
  public void takeChipsFromEveryPlayer() throws Exception {
    Board board = new Board(null, null);

    Player player1 = mock(Player.class);
    doNothing().when(player1).takeChips(anyInt());
    Player player2 = mock(Player.class);
    doNothing().when(player2).takeChips(anyInt());

    board.chipsOnBoard.put(player1, 10);
    board.chipsOnBoard.put(player2, 20);

    board.takeChipsFromEveryPlayer(5);

    assertEquals(new Integer(15), board.chipsOnBoard.get(player1));
    assertEquals(new Integer(25), board.chipsOnBoard.get(player2));

    verify(player1).takeChips(eq(5));
    verify(player2).takeChips(eq(5));
  }

  @Test
  public void getPlayerHand() throws Exception {
    Board board = new Board(null, null);

    Player human = new Human("player-1");
    Hand hand = new Hand() {{ add(new Card(ACE, SPADE)); }};
    board.hands.put(human, hand);

    assertEquals(hand, board.getPlayerHand(human));
  }

  @Test
  public void getMaxPossibleBid() throws Exception {
    Board board = new Board(null, null);

    Player player1 = mock(Player.class);
    doReturn(5).when(player1).chipCount();
    Player player2 = mock(Player.class);
    doReturn(7).when(player2).chipCount();

    board.chipsOnBoard.put(player1, 0);
    board.chipsOnBoard.put(player2, 0);

    assertEquals(5, board.getMaxPossibleBid());
  }

  @Test
  public void raiseBid() throws Exception {
    Board board = spy(new Board(null, null));

    doReturn(10).when(board).getMaxPossibleBid();
    doNothing().when(board).takeChipsFromEveryPlayer(anyInt());

    board.raiseBid(5);

    verify(board).getMaxPossibleBid();
    verify(board).takeChipsFromEveryPlayer(eq(5));
  }

  @Test
  public void findPlayersWithBestHand() throws Exception {
    Board board = spy(new Board(null, null));

    Player human = new Human("player-1");
    Player computer1 = new Computer("player-2");
    Player computer2 = new Computer("player-3");

    doReturn(new HashMap<Player, Long>(){{
      put(human, 20L);
      put(computer1, 10L);
      put(computer2, 20L);
    }}).when(board).getPlayersHandEvaluations();

    List<Player> playersWithBestHand = board.findPlayersWithBestHand();

    assertEquals(2, playersWithBestHand.size());
    assertTrue(playersWithBestHand.contains(human));
    assertTrue(playersWithBestHand.contains(computer2));
  }

  @Test
  public void getPlayersHandEvaluations() throws Exception {
    Player human = new Human("player-1");
    Player computer = new Computer("player-2");

    Hand hand1 = new Hand() {{ add(new Card(KING, SPADE)); }};
    Hand hand2 = new Hand() {{ add(new Card(ACE, SPADE)); }};

    HandEvaluator handEvaluator = mock(HandEvaluator.class);

    doReturn(10L).when(handEvaluator).evaluate(hand1);
    doReturn(20L).when(handEvaluator).evaluate(hand2);

    Board board = new Board(null, handEvaluator);
    board.hands.put(human, hand1);
    board.hands.put(computer, hand2);

    Map<Player, Long> evaluations = board.getPlayersHandEvaluations();
    assertEquals(2, evaluations.size());
    assertEquals(new Long(10), evaluations.get(human));
    assertEquals(new Long(20), evaluations.get(computer));
  }

  @Test
  public void giveChipsOnBoardToPlayer() throws Exception {
    Player player = mock(Player.class);
    doNothing().when(player).addChips(anyInt());

    Computer computer = new Computer("player-1");
    Human human = new Human("player-2");
    Board board = new Board(null, null);
    board.chipsOnBoard.put(computer, 2);
    board.chipsOnBoard.put(human, 5);
    board.chipsOnBoard.put(player, 9);

    board.giveChipsOnBoardToPlayer(player);

    verify(player).addChips(eq(2));
    verify(player).addChips(eq(5));
    verify(player).addChips(eq(9));

    assertEquals(new Integer(0), board.chipsOnBoard.get(computer));
    assertEquals(new Integer(0), board.chipsOnBoard.get(human));
    assertEquals(new Integer(0), board.chipsOnBoard.get(player));
  }

  @Test
  public void giveChipsBackToPlayers() throws Exception {
    Player player1 = mock(Player.class);
    Player player2 = mock(Player.class);

    doNothing().when(player1).addChips(anyInt());
    doNothing().when(player2).addChips(anyInt());

    Board board = new Board(null, null);
    board.chipsOnBoard.put(player1, 2);
    board.chipsOnBoard.put(player2, 3);

    board.giveChipsBackToPlayers();

    verify(player1).addChips(eq(2));
    verify(player2).addChips(eq(3));

    assertEquals(new Integer(0), board.chipsOnBoard.get(player1));
    assertEquals(new Integer(0), board.chipsOnBoard.get(player2));
  }

}