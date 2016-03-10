
public class BattleShip extends Ships {

	@Override
	// Function returns the name of the ship.
	public String getName() {
		String shipName = "BattleShip";
		return shipName;
	}

	@Override
	// Function returns the size of the ship.
	public int getPegCount() {
		int shipSize = 4;
		return shipSize;
	}

}
