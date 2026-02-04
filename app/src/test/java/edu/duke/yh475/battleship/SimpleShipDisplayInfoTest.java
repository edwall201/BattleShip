package edu.duke.yh475.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SimpleShipDisplayInfoTest {
  @Test
  public void test_getInfo() {
    SimpleShipDisplayInfo<Character> infor = new SimpleShipDisplayInfo<Character>('s', '*');
    Coordinate c= new Coordinate(1, 2);
    assertEquals('s', infor.getInfo(c, false));
    assertEquals('*', infor.getInfo(c, true));
  }

}
