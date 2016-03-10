import java.util.Random;
// This class will track the player's map and position of their ships.
public class Board {
  // The map is a 10x10 grid.
  public static int BOARD_SIZE = 10;
  public int board[][] = new int[BOARD_SIZE][BOARD_SIZE];
  public static Ships ships[] = { new PTboat(), new Destroyer(), new BattleShip(), new Carrier()};
  Random random = new Random();

  public Board() {
    // Fill the board with empty spots
    for(int row = 0; row < board.length; row++) {
      for(int col = 0; col < board[0].length; col++) {
        board[row][col] = 0;
      }
    }
  }

  public String toString() {
    // Build string representation of the board as a grid of ints
    StringBuilder sb = new StringBuilder();
    sb.append("+  1 2 3 4 5 6 7 8 9 10\n");
    for(int row = 0; row < board.length; row++) {
      sb.append("" + (row + 1) + (row < board.length - 1? " " : ""));
      for(int col = 0; col < board[0].length; col++) {
        sb.append(" " + board[row][col]);
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  public void populateBoard() {
    // Randomly place four ships
    for(int shipPegs = 2; shipPegs < 6; shipPegs++) {
    // goes to the ships array and starts at index[0] which creates a new ship
    Ships currentShip = ships[shipPegs-2];
     // gets the PegCount of the current ship in the loop
     int shipPegSize = currentShip.getPegCount();
    	// Loop until we've successfully placed this ship
      boolean finished = false;
      while(!finished) {
        // Pick a starting point on the board
        int startRow = random.nextInt(BOARD_SIZE);
        int startCol = random.nextInt(BOARD_SIZE);

        // Pick a direction to go (up, right, down, left) then convert that to board coordinates
        int direction = random.nextInt(4);
        int stepRow = 0, stepCol = 0;
        switch(direction) {
        //North 
        case 0: stepRow =  0; stepCol = -1; break; 
        //East  
        case 1: stepRow =  1; stepCol =  0; break; 
        //South  
        case 2: stepRow =  0; stepCol =  1; break; 
        //West  
        case 3: stepRow = -1; stepCol =  0; break; 
        }

        // Attempt to place the ship by 'walking forward' until we encounter an obstacle or finish
        int currentRow = startRow;
        int currentCol = startCol;
        boolean abortPlacement = false;
        for(int peg = 0; peg < shipPegSize; peg++) {
          if(  currentRow < 0 || currentRow > board.length - 1
            || currentCol < 0 || currentCol > board[0].length - 1
            || board[currentRow][currentCol] != 0 ) {
            // Current spot is either off the board or overlapping a ship, abort this attempt
            abortPlacement = true;
            break;
          }
          currentRow += stepRow;
          currentCol += stepCol;
        }
        if(abortPlacement) {
          continue;
        }
        // If we reach this point, we know the path is good, so place pegs as we go
        currentRow = startRow;
        currentCol = startCol;
        for(int peg = 0; peg < shipPegSize; peg++) {
          board[currentRow][currentCol] = shipPegSize;
          currentRow += stepRow;
          currentCol += stepCol;
        }
        finished = true;
      }
    }
  }

  private boolean shipExistsOnBoard(int shipType) {
    // check every square on the board if a given ship type exists
    // note that this assumes that there's only one ship of each type on the board
    for(int row = 0; row < board.length; row++) {
      for(int col = 0; col < board[0].length; col++) {
        if(board[row][col] == shipType) {
          return true;
        }
      }
    }
    return false;
  }

  // Scans the map for filled spaces. Returns true
  // if no filled spaces are found, false if ships
  // are on the map.
  private boolean boardIsEmpty() {
    for(int row = 0; row < board.length; row++) {
      for(int col = 0; col < board[0].length; col++) {
        if(board[row][col] != 0) {
          return false;
        }
      }
    }
    return true;
  }

  // Function accepts a row and column as parameters.
  // Will return an int based on the result of the shot.
  public int getShot(int row, int col) {
    int pegType = board[row][col];
    if(pegType == 0) {
      // Miss
      return 0;
    } else {
      board[row][col] = 0;
      if(!shipExistsOnBoard(pegType)) {
        if(boardIsEmpty()) {
          // Hit, sink & loss - this was the last peg
          return -1;
        }
        // Hit & sink
        return pegType;
      }
      // Hit
      return 1;
    }
  }

  // called when YOU have just sent a shot to the OPPONENT, and gotten a response (0, 1, ..., 5) from
  // the server indicating whether (or what) the shot hit
  public void markShot(int row, int col, int shipType) {
    if(shipType == 0) {
      board[row][col] = 8;
    } else {
      board[row][col] = shipType;
    }
  }
}
