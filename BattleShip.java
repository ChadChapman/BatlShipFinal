
/*
 * This class identifies the specific type of Ship, Battleship
 */
public class BattleShip extends Ships {

	/*
	 * This method returns the name of the type of ship.
	 * 
	 * @returns - String representing the type of ship, here a Battleship
	 */
	@Override
	public String getName() {
		String shipName = "BattleShip";
		return shipName;
	}

	/*
	 * This method returns the number of "pegs" of alloted amount of board
	 * squares for this type of ship.
	 * 
	 * @returns - int representing the "size" of ship, here a Battleship at 4
	 * squares
	 */
	@Override
	// Function returns the size of the ship.
	public int getPegCount() {
		int shipSize = 4;
		return shipSize;
	}

}
