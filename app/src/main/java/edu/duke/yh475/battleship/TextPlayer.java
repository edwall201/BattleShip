package edu.duke.yh475.battleship;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;


/**
 * TextPlayer handles the textual interaction with a player
 * manage input display board and executing placement phase
 */
public class TextPlayer {
  final Board<Character> theBoard;
  final BoardTextView view;
  final BufferedReader inputReader;
  final PrintStream out;
  final AbstractShipFactory<Character> shipFactory;
  final String name;
  final ArrayList<String> shipsToPlace;
  final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;

  /**
   * Construct a TextPlayer 
   * @param name is the name of the player
   * @param theBoard is the board where ships will be placed
   * @param inputSource is the source of user input
   * @param out is the output stream for messages
   * @param shipFactory is the factory used to create ship objects
   */
  public TextPlayer(String name, Board<Character> theBoard, BufferedReader inputSource, PrintStream out,
    AbstractShipFactory<Character> shipFactory) {
    this.name = name;
    this.theBoard = theBoard;
    this.view = new BoardTextView(theBoard);
    this.inputReader = inputSource;
    this.out = out;
    this.shipFactory = shipFactory;
    this.shipsToPlace = new ArrayList<>();
    this.shipCreationFns = new HashMap<>();

    setupShipCreationMap();
    setupShipCreatoinList();
 } 

  /**
   * Prompts the player for a aship placement and reads their response.
   * @param prompt is teh message to display to player
   * @param return a placement parsed from input
   * @throws EOFException if there is an error reading
   */
  public Placement readPlacement(String prompt) throws IOException {
    out.println(prompt);
    String s = inputReader.readLine();
    if(s == null){
      throw new EOFException("Player "+ name + " failed to enter a placement");
    }
    return new Placement(s);
  }

  /**
   * A single ship placement and display the board
   * @param shipName is the name of the ship to place
   * @param createFn is the function to create the ship based on placement
   * @throws IOException if input reading fails
   */
  public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException {
    while(true){
      try{
        String prompt = "Player " + name + " where do you want to place a " + shipName + "?";
        Placement p = readPlacement(prompt);
        Ship<Character> s = createFn.apply(p);
        String msg = theBoard.tryAddShip(s);
        if(msg == null){
          out.print(view.displayMyOwnBoard());
          return;
        }
        out.println(msg);
      }catch (IllegalArgumentException e){
       out.println("That placement is invalid: it does not have the correct format.");
      }
    }
  }

  /**
   * Displays the initial board, prints game instruction and handle ship placement
   * @throws IOException if there is an error during input output
   */
  public void doPlacementPhase() throws IOException {
    out.println(view.displayMyOwnBoard());
    String instruct = "Player " + name
        + ": you are going to place the following ships (which are all rectangular). For each ship, type the coordinate of the upper left side of the ship, followed by either H (for horizontal) or V (for vertical).  For example M4H would place a ship horizontally starting at M4 and going to the right.  You have\n"
        + "\n" + "2 \"Submarines\" ships that are 1x2\n" + "3 \"Destroyers\" that are 1x3\n"
        + "3 \"Battleships\" that are 1x4\n" + "2 \"Carriers\" that are 1x6\n";
    out.println(instruct);

    for(String shipName: shipsToPlace){
      Function<Placement, Ship<Character>> createFn = shipCreationFns.get(shipName);
      doOnePlacement(shipName, createFn);
    }
  }

  /**
   * Handles the ship placement phase for the player, prompting then to place each ship
   */
  protected void setupShipCreationMap(){
    shipCreationFns.put("Submarine", p -> shipFactory.makeSubmarine(p));
    shipCreationFns.put("Destroyer", p -> shipFactory.makeDestroyer(p));
    shipCreationFns.put("Battleship", p -> shipFactory.makeBattleship(p));
    shipCreationFns.put("Carrier", p -> shipFactory.makeCarrier(p));
  }

  /**
   * Handles the ship placement phase for the player, prompting then to place each ship
   */
  protected void setupShipCreatoinList(){
    shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
    shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
    shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
    shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
  }

  /**
   * Prompts the player for a coordinate and reads their response.
   * @param prompt is the message to display to player
   * @return a coordinate parsed from input
   * @throws IOException if there is an error input
   */
  protected Coordinate readCoordinate(String prompt) throws IOException{
    while(true){
      try{
        out.println(prompt);
        String input = inputReader.readLine();
        if(input == null){
          throw new EOFException("Input ended unexpectedly");
        }
        return new Coordinate(input);
      }catch(IllegalArgumentException e){
        out.println(e.getMessage() + " Please try again.");
      }
    }
  }
  
  /**
   * Manages a single turn of attacking the enemy board
   * @param enemyBoard is the board of the enemy player
   * @param enemyView is the view of the enemy board
   * @param myHeader is the header to display above the player's own board
   * @param enemyHeader is the header to display above the enemy board
   * @throws IOException if there is an error during input output
   */
  public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String myHeader, String enemyHeader) throws IOException{
    out.println(view.displayMyBoardWithEnemyNextToIt(enemyView, myHeader, enemyHeader));
    Coordinate coord = readCoordinate("Player " + name + "'s turn: input a coordinate to fire at");
    Ship<Character> hitRecord = enemyBoard.fireAt(coord);
    if (hitRecord != null){
      out.println("You hit a "+ hitRecord.getName() + "!");
    }else{
     out.println("You missed!\n");
    }
    
  }

  
} 
