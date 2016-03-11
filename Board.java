
/*
 * @Team Lowered Expectations
 * @CS 143
 * @Group Project Battleship Game
 * 
 * Since this is our first "major" file in alphabetical order, we will use this as a sort of main 
 * file for team information.
 * This class implements the board for the game and contains one constructor.
 * This class will track the player's map and position of their ships.
 */

import java.util.Random;

public class Board {
	// The map is a 10x10 grid.
	public static int BOARD_SIZE = 10;
	// our main data struct is a double array, not very sophisticated but the
	// best tool for the job that we found
	public int board[][] = new int[BOARD_SIZE][BOARD_SIZE];
	public static Ships ships[] = { new PTboat(), new Destroyer(), new BattleShip(), new Carrier() };
	Random random = new Random();

	/*
	 * Below is our constructor which populates the game board with available
	 * spaces labeled as "0" s
	 */
	public Board() {
		// Fill the board with empty spots
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				board[row][col] = 0;
			}
		}
	}

	/*
	 * This method builds a string representation of the board as a grid of ints
	 * 
	 * @returns - String representing the game board as ints
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("+  1 2 3 4 5 6 7 8 9 10\n");
		for (int row = 0; row < board.length; row++) {
			sb.append("" + (row + 1) + (row < board.length - 1 ? " " : ""));
			for (int col = 0; col < board[0].length; col++) {
				sb.append(" " + board[row][col]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/*
	 * This Method is the random Ship placement algorithm. While the boolean
	 * finished = false the method will continually try to populate the four
	 * ships.
	 * 
	 */
	public void populateBoard() {
		// Randomly place four ships
		for (int shipPegs = 2; shipPegs < 6; shipPegs++) {
			// goes to the ships array and starts at index[0] which creates a
			// new ship
			Ships currentShip = ships[shipPegs - 2];
			// gets the PegCount of the current ship in the loop
			int shipPegSize = currentShip.getPegCount();
			// Loop until we've successfully placed this ship
			boolean finished = false;
			// This loop will continue until all four ships are placed
			while (!finished) {
				// Pick a starting point on the board
				int startRow = random.nextInt(BOARD_SIZE);
				int startCol = random.nextInt(BOARD_SIZE);

				// Pick a direction to go (up, right, down, left) then convert
				// that to board coordinates
				int direction = random.nextInt(4);
				int stepRow = 0, stepCol = 0;
				switch (direction) {
				// Up
				case 0:
					stepRow = 0;
					stepCol = -1;
					break;
				// Right
				case 1:
					stepRow = 1;
					stepCol = 0;
					break;
				// Down
				case 2:
					stepRow = 0;
					stepCol = 1;
					break;
				// Left
				case 3:
					stepRow = -1;
					stepCol = 0;
					break;
				}

				// Attempt to place the ship by 'walking forward' until we
				// encounter an obstacle or finish
				int currentRow = startRow;
				int currentCol = startCol;
				boolean abortPlacement = false;
				// Checks to make sure the peg being placed is correctly on the
				// board
				// or overlaps with another ship
				for (int peg = 0; peg < shipPegSize; peg++) {
					if (currentRow < 0 || currentRow > board.length - 1 || currentCol < 0
							|| currentCol > board[0].length - 1 || board[currentRow][currentCol] != 0) {
						// Current spot is either off the board or overlapping a
						// ship, abort this attempt
						abortPlacement = true;
						break;
					}

					currentRow += stepRow;
					currentCol += stepCol;
				}
				if (abortPlacement) {
					continue;
				}
				// If we reach this point, we know the path is good, so place
				// pegs as we go
				currentRow = startRow;
				currentCol = startCol;
				// Places the current ships pegs on the board
				for (int peg = 0; peg < shipPegSize; peg++) {
					board[currentRow][currentCol] = shipPegSize;
					currentRow += stepRow;
					currentCol += stepCol;
				}
				finished = true;
			}
		}
	}

	/*
	 * This method checks every square on the board to see if a given ship type
	 * exists. Note that this assumes that there's only one ship of each type on
	 * the board.
	 * 
	 * @ returns - boolean representing whether or not the Ship type in the
	 * method parameter is on the game board
	 * 
	 * @param - int that denotes the type of ship to check the board for,
	 * example 3 = check for a submarine, which has 3 spaces.
	 */
	private boolean shipExistsOnBoard(int shipType) {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col] == shipType) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * This method scans the map for filled spaces. Returns true if no filled
	 * spaces are found, false if ships are on the map.
	 * 
	 * @returns - boolean representing whether or not the game board has any
	 * ships placed or remaining
	 */
	private boolean boardIsEmpty() {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col] != 0) {
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * This method accepts a row and column as parameters and will return an int
	 * based on the result of the shot.
	 * 
	 * @param1 - int representing the row number of a shot's coordinates
	 * 
	 * @param2 - int representing the column number of a shot's coordinates
	 * 
	 * @returns - int representing if a shot was a hit or miss, 0 = miss and 1 =
	 * hit
	 */
	public int getShot(int row, int col) {
		int pegType = board[row][col];
		if (pegType == 0) {
			// Miss
			return 0;
		} else {
			board[row][col] = 0;
			if (!shipExistsOnBoard(pegType)) {
				if (boardIsEmpty()) {
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

	/*
	 * This method is called when the Player representing YOU have just sent a
	 * shot to the OPPONENT Player, and gotten a response (0, 1, ..., 5) from
	 * the server indicating whether (or what) the shot hit
	 * 
	 * @param1 - int representing the row number of the shot
	 * 
	 * @param2 - int representing the column number of the shot
	 * 
	 * @param3 - int representing the type of ship, example 3 = submarine
	 * 
	 */
	public void markShot(int row, int col, int shipType) {
		if (shipType == 0) {
			board[row][col] = 8;
		} else {
			board[row][col] = shipType;
		}
	}
}
