package com.nortal.game.poker.ui;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ConsoleUITest {
  @Test
  public void getExtraBid() throws Exception {
    ConsoleUI ui = spy(new ConsoleUI());
    doNothing().when(ui).displayToUser(anyString());
    ByteArrayInputStream inputStream = new ByteArrayInputStream("4".getBytes());
    ui.consoleInputReader = new Scanner(inputStream);
    assertEquals(4, ui.getExtraBid(5));
    verify(ui).displayToUser(eq("Raise your bid (0 - 5):"));
  }

  @Test
  public void getExtraBidNotANumber() throws Exception {
    ConsoleUI ui = spy(new ConsoleUI());
    doNothing().when(ui).displayToUser(anyString());
    ByteArrayInputStream inputStream = new ByteArrayInputStream("a".getBytes());
    ui.consoleInputReader = new Scanner(inputStream);

    assertEquals(0, ui.getExtraBid(5));
    verify(ui).displayToUser(eq("Bid must be a number"));
  }

  @Test
  public void getExtraBidTooLarge() throws Exception {
    ConsoleUI ui = spy(new ConsoleUI());
    doNothing().when(ui).displayToUser(anyString());
    ByteArrayInputStream inputStream = new ByteArrayInputStream("4\n2".getBytes());
    ui.consoleInputReader = new Scanner(inputStream);

    assertEquals(2, ui.getExtraBid(2));
    verify(ui).displayToUser(eq("Bid must be between 0 and 2"));
  }

}