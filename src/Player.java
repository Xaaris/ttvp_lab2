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
		playerFields = new Field[Constants.NUMBEROFFIELDSINSECTOR];
		BigInteger sectorSize = startID.distanceTo(endID);
		BigInteger numberOfFieldsInSector = new BigInteger("" + Constants.NUMBEROFFIELDSINSECTOR);
		BigInteger fieldSize = sectorSize.divide(numberOfFieldsInSector);
//		BigInteger rest = sectorSize.mod(numberOfFieldsInSector);

		BigInteger currentBigInt = startID.toBigInteger();
		for (int i = 0; i < Constants.NUMBEROFFIELDSINSECTOR - 1; i++) {
			ID lowerID = new ID(currentBigInt.toByteArray());
			BigInteger upperIDAsBigInt = currentBigInt.add(fieldSize).subtract(BigInteger.ONE);
			upperIDAsBigInt = upperIDAsBigInt.mod(Constants.MAXVALUE);
			upperIDAsBigInt = Util.shortenTo20Bytes(upperIDAsBigInt);
			ID upperID = ID.valueOf(upperIDAsBigInt);
			currentBigInt = currentBigInt.add(fieldSize);
			currentBigInt = currentBigInt.mod(Constants.MAXVALUE);
			currentBigInt = Util.shortenTo20Bytes(currentBigInt);

			Field tmpField = new Field(lowerID, upperID);
			playerFields[i] = tmpField;
		}

		ID lowerID = new ID(currentBigInt.toByteArray());
		playerFields[Constants.NUMBEROFFIELDSINSECTOR - 1] = new Field(lowerID, endID);

	}

	public ID getStartID() {
		return startID;
	}

	public ID getEndID() {
		return endID;
	}
	
	public void changeSectorSize(ID newStartID, ID newEndID){
		this.startID = newStartID;
		this.endID = newEndID;
		BigInteger sectorSize = startID.distanceTo(endID);
		BigInteger numberOfFieldsInSector = new BigInteger("" + Constants.NUMBEROFFIELDSINSECTOR);
		BigInteger fieldSize = sectorSize.divide(numberOfFieldsInSector);
//		BigInteger rest = sectorSize.mod(numberOfFieldsInSector);

		BigInteger currentBigInt = startID.toBigInteger();
		for (int i = 0; i < Constants.NUMBEROFFIELDSINSECTOR - 1; i++) {
			ID lowerID = new ID(currentBigInt.toByteArray());
			BigInteger upperIDAsBigInt = currentBigInt.add(fieldSize).subtract(BigInteger.ONE);
			upperIDAsBigInt = upperIDAsBigInt.mod(Constants.MAXVALUE);
			upperIDAsBigInt = Util.shortenTo20Bytes(upperIDAsBigInt);
			ID upperID = ID.valueOf(upperIDAsBigInt);
			currentBigInt = currentBigInt.add(fieldSize);
			currentBigInt = currentBigInt.mod(Constants.MAXVALUE);
			currentBigInt = Util.shortenTo20Bytes(currentBigInt);

			playerFields[i].setStartID(lowerID);
			playerFields[i].setEndID(upperID);
		}

		ID lowerID = new ID(currentBigInt.toByteArray());
		playerFields[Constants.NUMBEROFFIELDSINSECTOR - 1].setStartID(lowerID);
		playerFields[Constants.NUMBEROFFIELDSINSECTOR - 1].setStartID(endID);
	}

	public Field getFieldForID(ID id) {
		if (isIDInPlayerSector(id)) {
			for (Field field : playerFields) {
				if (field.isIDInField(id)){
					return field;
				}
			}
			System.out.println("SOMETHING WENT WRONG.. Field not found");
			return null;
		} else {
			System.out.println("SOMETHING WENT WRONG.. Field is not in playersector");
			return null;
		}
	}

	public boolean isIDInPlayerSector(ID id) {
		if (ID.valueOf(endID.toBigInteger().add(BigInteger.ONE)).equals(startID)){
			return true;
		}else{
			return id.isInInterval(ID.valueOf(startID.toBigInteger().subtract(BigInteger.ONE)), ID.valueOf(endID.toBigInteger().add(BigInteger.ONE)));
		}
//		System.out.println("isIDInPlayerSector");
//		System.out.println("startID: " + startID.shortIDAsString());
//		System.out.println("endID: " + endID.shortIDAsString());
//		System.out.println("id to compare: " + id.shortIDAsString());
//		System.out.println("compare to gives: " + id.distanceTo(endID).compareTo(startID.distanceTo(endID)) );
//		System.out.println("returns: " + (id.distanceTo(endID).compareTo(startID.distanceTo(endID)) > 0));
//		if (id.distanceTo(endID).compareTo(startID.distanceTo(endID)) > 0) {
//			return false;
//		}
//		return true;
	}
	
	public Field[] getPlayerFields(){
		return playerFields;
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
	
	public String toString(){
		return "Player [" +startID.shortIDAsString() + " - " + endID.shortIDAsString() + "]";
	}

}
