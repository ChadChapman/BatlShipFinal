
public class Carrier extends Ships {

	@Override
	// Function returns the name of the ship.
	public String getName() {
		String shipName = "Carrier";
		return shipName;
	}

	@Override
	// Function returns the size of the ship.
	public int getPegCount() {
		int shipSize = 5;
		return shipSize;
	}

}
