/*
 * This class identifies the specific type of Ship, Destroyer
 */

public class Destroyer extends Ships {

	/*
	 * This method returns the name of the type of ship.
	 * 
	 * @returns - String representing the type of ship, here a Destroyer
	 */
	@Override
	public String getName() {
		String shipName = "Destroyer";
		return shipName;
	}
	/*
	 * This method returns the number of "pegs" of alloted amount of board
	 * squares for this type of ship.
	 * 
	 * @returns - int representing the "size" of ship, here a Destroyer at 3
	 * squares
	 */
	@Override
	public int getPegCount() {
		int shipSize = 3;
		return shipSize;
	}

}
