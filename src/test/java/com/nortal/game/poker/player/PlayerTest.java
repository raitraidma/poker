package com.nortal.game.poker.player;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerTest {

  @Test
  public void setChip() throws Exception {
    PlayerImpl player = new PlayerImpl("name");
    player.setChips(666);
    assertEquals(666, player.chips);
  }

  @Test
  public void addChips() throws Exception {
    PlayerImpl player = new PlayerImpl("name");
    player.chips = 3;
    player.addChips(2);
    assertEquals(5, player.chips);
  }

  @Test
  public void takeChips() throws Exception {
    PlayerImpl player = new PlayerImpl("name");
    player.chips = 3;
    player.takeChips(2);
    assertEquals(1, player.chips);
  }

  @Test(expected = RuntimeException.class)
  public void takeChipsThrowsException() throws Exception {
    PlayerImpl player = new PlayerImpl("name");
    player.chips = 3;
    player.takeChips(4);
  }

  @Test
  public void hasChips() throws Exception {
    PlayerImpl player = new PlayerImpl("name");
    player.chips = 3;
    assertTrue(player.hasChips());
  }

  @Test
  public void hasNoChips() throws Exception {
    PlayerImpl player = new PlayerImpl("name");
    player.chips = 0;
    assertFalse(player.hasChips());
  }

  @Test
  public void chipCount() throws Exception {
    PlayerImpl player = new PlayerImpl("name");
    player.chips = 69;
    assertEquals(69, player.chipCount());
  }

  @Test
  public void getName() throws Exception {
    PlayerImpl player = new PlayerImpl("My Name");
    assertEquals("My Name", player.getName());
  }

  class PlayerImpl extends Player {
    public PlayerImpl(String name) {
      super(name);
    }
  }
}