
//Imports the necessary packages.
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Game {

	Board myBoard = new Board(); // Creates an instance of Board for the player.
	Board theirBoard = new Board(); // Creates an instance of Board for the
									// opponent.
	Client client = new Client(); // Starts the client for communication.
	Random random = new Random();

	// The constructor for Game will assign locations
	// of the ships to the players map.
	public Game() {
		myBoard.populateBoard();
	}

	/*
	 * This function is responsible for the controlling the play of the game. It
	 * alternates sending shots back and forth between players until one player
	 * loses. Prints an updated map after each turn.
	 * 
	 * @throws - IOException representing a connection error
	 */
	public void run() throws IOException {
		boolean goingFirstTurn = client.connect(); // Connects the client and
													// finds out who goes first.
		boolean isFirstTurn = true;

		// Prints a representation of the player's map.
		System.out.println("Your board:");
		System.out.println(myBoard);

		boolean gameIsOver = false;
		// Continues turns until game ends.
		while (!gameIsOver) {
			// Fires a shot and prints updated map.
			if (goingFirstTurn) {
				System.out.println("You go first!");
				fireShot();
				System.out.println("Their board:");
				System.out.println(theirBoard);
				goingFirstTurn = false;
			}
			// Waits if it's not players turn.
			if (isFirstTurn && !goingFirstTurn) {
				System.out.println("You go second.");
				isFirstTurn = false;
			}
			// Receives shot from opponent.
			System.out.println("Waiting for enemy shot...");
			if (receiveShot()) {
				gameIsOver = true;
				break;
			}
			System.out.println("Your board:");
			System.out.println(myBoard);
			if (fireShot()) {
				gameIsOver = true;
				break;
			}
			System.out.println("Their board (1 == hit, 2-5 == last hit on a ship, 8 == miss):");
			System.out.println(theirBoard);
		}
	}

	/*
	 * Function asks user for the location of the shot they would like to fire.
	 * It is sent through the client to the server and returns true for hit or
	 * false for miss.
	 * 
	 * @returns - boolean representing hit or miss for Player shot
	 * 
	 * @throws - IOException representing a connection error
	 * 
	 */
	private boolean fireShot() throws IOException {

		Scanner input = new Scanner(System.in);
		boolean coordsValid = false;
		// This While loop will Check for out of bounds numbers until valid ones
		// are input
		while (!coordsValid) {
			System.out.println("Enter two numbers:");
			int shotRow = input.nextInt() - 1;
			int shotCol = input.nextInt() - 1;
			// this if statement checks row and column to make sure shots are
			// inbounds
			if (shotRow < 0 || shotRow > Board.BOARD_SIZE || shotCol < 0 || shotCol > Board.BOARD_SIZE) {
				System.out.println("Invalid Numbers PLease Try Again");
				coordsValid = false;
				// restarts the loop
				continue;
			} else
				// Breaks the loop coords are good
				coordsValid = true;

			int shotResult = client.sendShot(shotRow, shotCol);
			// Processes the result the shot from the client.
			if (shotResult > 1) {
				String sunkShipType = Board.ships[shotResult - 2].getName();
				System.out.println("HIT! Sunk enemy " + sunkShipType + "!");
			} else if (shotResult == 1) {
				System.out.println("Hit!");
			} else if (shotResult == -1) {
				System.out.println("HIT! You sunk the last enemy ship, you win!");
				return true;
			} else {
				System.out.println("Miss...");
			}
			theirBoard.markShot(shotRow, shotCol, shotResult);
		}
		return false;
	}

	/*
	 * Function will receive a shot from the client, check if the shot hit a
	 * ship. It sends the result of the shot to the client and return it to the
	 * caller.
	 * 
	 * @returns - boolean representing if Player shot was received
	 * 
	 * @throws - IOException representing connection error
	 * 
	 */
	private boolean receiveShot() throws IOException {
		int incoming[] = client.receiveShot(); // Receives the shot array from
												// the client.
		int shotResult = myBoard.getShot(incoming[0], incoming[1]);
		// Prints information to the console based on the result of
		// the shot from the opponent.
		if (shotResult > 1) {
			String sunkShipType = Board.ships[shotResult - 2].getName();
			System.out.println("The enemy sank your " + sunkShipType + "!");
		} else if (shotResult == 1) {
			System.out.println("Enemy hit at " + (incoming[0] + 1) + ", " + (incoming[1] + 1) + "!");
		} else if (shotResult == -1) {
			System.out.println("Enemy hit! Your last ship is sunk, you lose!");
			client.sendShotResult(shotResult);
			return true;
		} else {
			System.out.println("Enemy miss at " + (incoming[0] + 1) + ", " + (incoming[1] + 1) + ".");
		}
		client.sendShotResult(shotResult);
		return false;
	}

	/*
	 * This method returns the calling Player's game board
	 * 
	 * @returns - Object representing Your game board with Your ships
	 */
	public Board getMyBoard() {
		return myBoard;
	}

	/*
	 * This method returns the opposing Player's game board
	 * 
	 * @returns - Object representing Opponents game board with Your shots taken
	 * but no ships showing obviously
	 */

	public Board getTheirBoard() {
		return theirBoard;
	}

	////////////////////////////////////////////////////////////////////////////////
	//
	/*
	 * Function attempts to start a new game and connect to the server.
	 * 
	 * @param1 - String array typical with main methods
	 */
	public static void main(String[] args) {
		Game game = new Game();
		try {
			game.run();
		} catch (IOException e) {
			System.err.println("Error: Failed to communicate with server, closing.");
		}
	}
}
