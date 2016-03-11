
/*
 * This class identifies the specific type of Ship, Carrier
 */
public class Carrier extends Ships {

	/*
	 * This method returns the name of the type of ship.
	 * 
	 * @returns - String representing the type of ship, here a Carrier
	 */
	@Override
	public String getName() {
		String shipName = "Carrier";
		return shipName;
	}

	/*
	 * This method returns the number of "pegs" of alloted amount of board
	 * squares for this type of ship.
	 * 
	 * @returns - int representing the "size" of ship, here a Battleship at 5
	 * squares
	 */
	@Override
	public int getPegCount() {
		int shipSize = 5;
		return shipSize;
	}

}
