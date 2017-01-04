package haw;

public class RandomShipCreator implements ShipCreator {

	public static FieldState[] generatRandomShipFormation() {
		FieldState[] sea = new FieldState[Constants.NUMBEROFFIELDSINSECTOR];

		for (int i = 0; i < Constants.NUMBEROFFIELDSINSECTOR; i++) {
				sea[i] = FieldState.WATER;
		}

		for (int i = 0; i < Constants.MAXSHIPS; i++) {
			int randomFieldIndex = 0;

			do {
				randomFieldIndex = (int) (Math.random() * sea.length);
			} while (sea[randomFieldIndex] == FieldState.SHIP);

			sea[randomFieldIndex] = FieldState.SHIP;
		}

		return sea;
	}

	@Override
	public void createShips(Field[] playerFields) {
		FieldState[] fieldstates = generatRandomShipFormation();

		for (int i = 0; i < playerFields.length; i++) {
			playerFields[i].setState(fieldstates[i]);
		}

	}
}