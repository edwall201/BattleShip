package edu.duke.yh475.battleship;

public class V1ShipFactory implements AbstractShipFactory<Character> {
  /**
   * To create a Rectangleship base on the provide placement
   *@param where the placement for the ship
   *@param w the width of the ship
   *@param h the height of the ship
   *@param name the descriptive name of the ship
   *@param letter the character used for display
   *@return A new RectangleShip
   */
  protected Ship<Character> createShip(Placement where, int w, int h, char letter, String name) {
    char orientation = where.getOrientation();
    int new_w = 0;
    int new_h = 0;
    if (orientation == 'V') {
      new_w = w;
      new_h = h;
    } else {
      new_w = h;
       new_h = w;
    }
    return new RectangleShip<Character>(name, where.getWhere(), new_w, new_h, letter, '*');
  }

  @Override
  public Ship<Character> makeBattleship(Placement where) {
    return createShip(where, 1, 4, 'b', "BattleShip");
  }

  @Override
  public Ship<Character> makeDestroyer(Placement where) {
    return createShip(where, 1, 3, 'd', "Destroy");
  }

  @Override
  public Ship<Character> makeSubmarine(Placement where) {
    return createShip(where, 1, 2, 's', "Submarine");
  }

  @Override
  public Ship<Character> makeCarrier(Placement where) {
    return createShip(where, 1, 6, 'c', "Carrier");
  }

}
