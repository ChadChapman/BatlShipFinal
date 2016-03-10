
public class Carrier extends Ships {

	@Override
	public String getName() {
		String shipName = "Carrier";
		return shipName;
	}

	@Override
	public int getPegCount() {
		int shipSize = 5;
		return shipSize;
	}

}
