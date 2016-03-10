
public class BattleShip extends Ships {

	@Override
	// Name of the Boat
	public String getName() {
		String shipName = "BattleShip";
		return shipName;
	}

	@Override
	//Size of the boat
	public int getPegCount() {
		int shipSize = 4;
		return shipSize;
	}

}
