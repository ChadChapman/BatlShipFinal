import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Game {

  Board myBoard = new Board();
  Board theirBoard = new Board();
  Client client = new Client();
  Random random = new Random();

  public Game() {
    myBoard.populateBoard();
  }

  public void run() throws IOException {
    boolean goingFirstTurn = client.connect();
    boolean isFirstTurn = true;
    System.out.println("Your board:");
    System.out.println(myBoard);

    boolean gameIsOver = false;
    while(!gameIsOver) {
    	
      if(goingFirstTurn) {
        System.out.println("You go first!");
        fireShot();
        System.out.println("Their board:");
        System.out.println(theirBoard);
        goingFirstTurn = false;
      }
      if(isFirstTurn && !goingFirstTurn) {
        System.out.println("You go second.");
        isFirstTurn = false;
      }
      System.out.println("Waiting for enemy shot...");
      if(receiveShot()) {
        gameIsOver = true;
        break;
      }
      System.out.println("Your board:");
      System.out.println(myBoard);
      if(fireShot()) {
        gameIsOver = true;
        break;
      }
      System.out.println("Their board (1 == hit, 2-5 == last hit on a ship, 8 == miss):");
      System.out.println(theirBoard);
    }
  }

  private boolean fireShot() throws IOException {
    System.out.println("Enter two numbers:");
    Scanner input = new Scanner(System.in);
    int shotRow = input.nextInt() - 1;
    int shotCol = input.nextInt() - 1;

    int shotResult = client.sendShot(shotRow, shotCol);
    if(shotResult > 1) {
      String sunkShipType = Board.ships[shotResult-2].getName();
      System.out.println("HIT! Sunk enemy " + sunkShipType + "!");
    } else if(shotResult == 1) {
      System.out.println("Hit!");
    } else if(shotResult == -1) {
      System.out.println("HIT! You sunk the last enemy ship, you win!");
      return true;
    } else {
      System.out.println("Miss...");
    }
    theirBoard.markShot(shotRow, shotCol, shotResult);
    return false;
  }

  private boolean receiveShot() throws IOException {
    int incoming[] = client.receiveShot();
    int shotResult = myBoard.getShot(incoming[0], incoming[1]);
    if(shotResult > 1) {
      String sunkShipType = Board.ships[shotResult-2].getName();
      System.out.println("The enemy sank your " + sunkShipType + "!");
    } else if(shotResult == 1) {
      System.out.println("Enemy hit at " + (incoming[0]+1) + ", " + (incoming[1]+1) + "!");
    } else if(shotResult == -1) {
      System.out.println("Enemy hit! Your last ship is sunk, you lose!");
      client.sendShotResult(shotResult);
      return true;
    } else {
      System.out.println("Enemy miss at " + (incoming[0]+1) + ", " + (incoming[1]+1) + ".");
    }
    client.sendShotResult(shotResult);
    return false;
  }

  public Board getMyBoard() {
    return myBoard;
  }

  public Board getTheirBoard() {
    return theirBoard;
  }

  ////////////////////////////////////////////////////////////////////////////////

  public static void main(String[] args) {
    Game game = new Game();
    try {
      game.run();
    } catch(IOException e) {
      System.err.println("Error: Failed to communicate with server, closing.");
    }
  }
}
