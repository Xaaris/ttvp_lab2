import java.math.BigInteger;

import de.uniba.wiai.lspi.chord.data.ID;

public class Player {

	private ID startID;
	private ID endID; // Players position
	private Field[] playerFields;

	public Player(ID startID, ID endID) {
		this.startID = startID;
		this.endID = endID;
		initializeSector();
	}

	public int getNumberOfShipsLeft() {
		int countOfShipsLeft = Constants.MAXSHIPS;
		for (Field field : playerFields) {
			if (field.getState() == FieldState.SHIPWRECK) {
				countOfShipsLeft--;
			}
		}
		return countOfShipsLeft;
	}

	public int getNumberOfReceivedUniqueShots() {
		int countOfFieldsNotUnknownState = 0;
		for (Field field : playerFields) {
			if (field.getState() != FieldState.UNKNOWN) {
				countOfFieldsNotUnknownState++;
			}
		}
		return countOfFieldsNotUnknownState;
	}

	public double getHitChanceForPlayer() {
		int remainingShips = getNumberOfShipsLeft();
		int remainingUnknownFields = playerFields.length - getNumberOfReceivedUniqueShots();
		return (1.0 / remainingUnknownFields) * remainingShips;
	}

	public void initializeSector() {
		BigInteger sectorSize = startID.distanceTo(endID);
		BigInteger numberOfFieldsInSector = new BigInteger("" + Constants.NUMBEROFFIELDSINSECTOR);
		BigInteger fieldSize = sectorSize.divide(numberOfFieldsInSector);
//		BigInteger rest = sectorSize.mod(numberOfFieldsInSector);

		BigInteger currentBigInt = startID.toBigInteger();
		for (int i = 0; i < Constants.NUMBEROFFIELDSINSECTOR - 1; i++) {
			ID lowerID = new ID(currentBigInt.toByteArray());
			ID upperID = new ID(currentBigInt.add(fieldSize).subtract(new BigInteger("1")).toByteArray());
			currentBigInt = currentBigInt.add(fieldSize);

			Field tmpField = new Field(lowerID, upperID);
			playerFields[i] = tmpField;
		}

		ID lowerID = new ID(currentBigInt.toByteArray());
		playerFields[Constants.NUMBEROFFIELDSINSECTOR - 0] = new Field(lowerID, endID);

	}

	public Field getFieldForID(ID id) {
		if (isIDInPlayerSector(id)) {
			for (Field field : playerFields) {
				if (field.isIDInField(id)){
					return field;
				}
			}
			return null;
		} else {
			return null;
		}
	}

	public boolean isIDInPlayerSector(ID id) {
		if (id.distanceTo(endID).compareTo(startID.distanceTo(endID)) > 0) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endID == null) ? 0 : endID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (endID == null) {
			if (other.endID != null)
				return false;
		} else if (!endID.equals(other.endID))
			return false;
		return true;
	}

}
