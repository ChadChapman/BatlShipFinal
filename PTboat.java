
/*
 * This class identifies the specific type of Ship, PTBoat
 */

public class PTboat extends Ships {

	/*
	 * This method returns the name of the type of ship.
	 * 
	 * @returns - String representing the type of ship, here a PTBoat
	 */

	@Override
	public String getName() {
		String shipName = "PTboat";
		return shipName;
	}

	/*
	 * This method returns the number of "pegs" of alloted amount of board
	 * squares for this type of ship.
	 * 
	 * @returns - int representing the "size" of ship, here a PTBoat at 2
	 * squares
	 */
	@Override
	public int getPegCount() {
		int shipSize = 2;
		return shipSize;
	}

}
