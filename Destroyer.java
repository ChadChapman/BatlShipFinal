
public class Destroyer extends Ships {

	@Override
	public String getName() {
		String shipName = "Destroyer";
		return shipName;
	}

	@Override
	public int getPegCount() {
		int shipSize = 3;
		return shipSize;
	}

}
