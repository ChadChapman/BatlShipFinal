
public class Destroyer extends Ships {

	@Override
	// Function returns the name of the ship.
	public String getName() {
		String shipName = "Destroyer";
		return shipName;
	}

	@Override
	// Function returns the size of the ship.
	public int getPegCount() {
		int shipSize = 3;
		return shipSize;
	}

}
