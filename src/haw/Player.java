package haw;
import java.math.BigInteger;

import de.uniba.wiai.lspi.chord.data.ID;

/**
 * 
 * @author Johannes & Erik
 * Class describing one player
 */
public class Player {

	private ID startID;
	private ID endID; // Players position
	private Field[] playerFields;

	public Player(ID startID, ID endID) {
		this.startID = startID;
		this.endID = endID;
		initializeSector();
	}

	/**
	 * 
	 * @return int: number of ships the player has left
	 */
	public int getNumberOfShipsLeft() {
		int countOfShipsLeft = Constants.MAXSHIPS;
		for (Field field : playerFields) {
			if (field.getState() == FieldState.SHIPWRECK) {
				countOfShipsLeft--;
			}
		}
		return countOfShipsLeft;
	}
	
	/**
	 * returns how many shots were received by this player
	 * @return
	 */
	public int getNumberOfReceivedUniqueShots() {
		int numberOfShots = 0;
		for (Field field : playerFields) {
			if (field.getState() == FieldState.WATER_SHOT_AT || field.getState() == FieldState.SHIPWRECK) {
				numberOfShots++;
			}
		}
		return numberOfShots;
	}

	/**
	 * returns the chance for a hit when shooting at this player
	 * @return
	 */
	public double getHitChanceForPlayer() {
		int remainingShips = getNumberOfShipsLeft();
		int remainingUnknownFields = playerFields.length - getNumberOfReceivedUniqueShots();
		return (1.0 / remainingUnknownFields) * remainingShips;
	}

	/**
	 * creates the player's fields and initilizes them to Unknown
	 */
	public void initializeSector() {
		playerFields = new Field[Constants.NUMBEROFFIELDSINSECTOR];
		BigInteger sectorSize = startID.distanceTo(endID);
		BigInteger numberOfFieldsInSector = BigInteger.valueOf(Constants.NUMBEROFFIELDSINSECTOR);
		BigInteger fieldSize = sectorSize.divide(numberOfFieldsInSector);
//		BigInteger rest = sectorSize.mod(numberOfFieldsInSector);
//
		BigInteger startBigInt = startID.toBigInteger();
		for (int i = 0; i < Constants.NUMBEROFFIELDSINSECTOR; i++) {
			ID lowerID = ID.valueOf((fieldSize.multiply(BigInteger.valueOf(i)).add(startBigInt)).mod(Constants.MAXVALUE));
			ID upperID = ID.valueOf((fieldSize.multiply(BigInteger.valueOf(i + 1)).add(startBigInt)).mod(Constants.MAXVALUE));
			Field tmpField = new Field(lowerID, upperID, FieldState.UNKNOWN);
			playerFields[i] = tmpField;
		}
		playerFields[Constants.NUMBEROFFIELDSINSECTOR - 1].setEndID(endID);
//		BigInteger currentBigInt = startID.toBigInteger();
//		for (int i = 0; i < Constants.NUMBEROFFIELDSINSECTOR - 1; i++) {
//			ID lowerID = new ID(currentBigInt.toByteArray());
//			BigInteger upperIDAsBigInt = Util.sanitizeBigInt(currentBigInt.add(fieldSize).subtract(BigInteger.ONE));
//			upperIDAsBigInt = Util.sanitizeBigInt(upperIDAsBigInt);
//			ID upperID = ID.valueOf(upperIDAsBigInt);
//			currentBigInt = currentBigInt.add(fieldSize);
//			currentBigInt = Util.sanitizeBigInt(currentBigInt);
//
//			Field tmpField = new Field(lowerID, upperID, FieldState.UNKNOWN);
//			playerFields[i] = tmpField;
//		}
//
//		ID lowerID = new ID(currentBigInt.toByteArray());
//		playerFields[Constants.NUMBEROFFIELDSINSECTOR - 1] = new Field(lowerID, endID, FieldState.UNKNOWN);
//
	}

	public ID getStartID() {
		return startID;
	}

	public ID getEndID() {
		return endID;
	}
	
	/**
	 * changes this players sector to new size
	 * keeps all field's states
	 * @param newStartID
	 * @param newEndID
	 */
	public void changeSectorSize(ID newStartID, ID newEndID){
		this.startID = newStartID;
		this.endID = newEndID;
		BigInteger sectorSize = startID.distanceTo(endID);
		BigInteger numberOfFieldsInSector = BigInteger.valueOf(Constants.NUMBEROFFIELDSINSECTOR);
		BigInteger fieldSize = sectorSize.divide(numberOfFieldsInSector);
//		System.out.println("NUMBEROFFIELDSINSECTOR: " + numberOfFieldsInSector);
//		System.out.println("SECTORSIZE: " + sectorSize);
//		System.out.println("FIELDSIZE: " + fieldSize);
//		System.err.println("Start and endID: " + startID + " " + endID);
//		System.err.println("First Field: " + playerFields[0]);
//		BigInteger rest = sectorSize.mod(numberOfFieldsInSector);

//		BigInteger currentBigInt = Util.sanitizeBigInt(startID.toBigInteger());
//		for (int i = 0; i < Constants.NUMBEROFFIELDSINSECTOR - 1; i++) {
//			ID lowerID = new ID(currentBigInt.toByteArray());
//			BigInteger upperIDAsBigInt = Util.sanitizeBigInt(currentBigInt.add(fieldSize).subtract(BigInteger.ONE));
//			upperIDAsBigInt = Util.sanitizeBigInt(upperIDAsBigInt);
//			ID upperID = ID.valueOf(upperIDAsBigInt);
//			currentBigInt = currentBigInt.add(fieldSize);
//			currentBigInt = Util.sanitizeBigInt(currentBigInt);
//
//			playerFields[i].setStartID(lowerID);
//			playerFields[i].setEndID(upperID);
//		}
//
//		ID lowerID = new ID(currentBigInt.toByteArray());
//		playerFields[Constants.NUMBEROFFIELDSINSECTOR - 1].setStartID(lowerID);
//		playerFields[Constants.NUMBEROFFIELDSINSECTOR - 1].setStartID(endID);
//		System.out.println();
//		System.err.println("Start and endID: " + startID + " " + endID);
//		System.err.println("First Field: " + playerFields[0]);
		
		BigInteger startBigInt = startID.toBigInteger();
		for (int i = 0; i < Constants.NUMBEROFFIELDSINSECTOR; i++) {
			ID lowerID = ID.valueOf((fieldSize.multiply(BigInteger.valueOf(i)).add(startBigInt)).mod(Constants.MAXVALUE));
			ID upperID = ID.valueOf((fieldSize.multiply(BigInteger.valueOf(i + 1)).add(startBigInt)).mod(Constants.MAXVALUE));
			playerFields[i].setStartID(lowerID);
			playerFields[i].setEndID(upperID);
		}
		playerFields[Constants.NUMBEROFFIELDSINSECTOR - 1].setEndID(endID);
	}

	/**
	 * returns the corresponding Field for id
	 * returns null on Error
	 * @param id
	 * @return
	 */
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

	/**
	 * returns true if id is in this player's sector, else false
	 * @param id
	 * @return
	 */
	public boolean isIDInPlayerSector(ID id) {
		if (ID.valueOf(Util.sanitizeBigInt(endID.toBigInteger().add(BigInteger.ONE))).equals(startID)){
			return true;
		}else{
			return id.isInInterval(ID.valueOf(Util.sanitizeBigInt(startID.toBigInteger().subtract(BigInteger.ONE))), ID.valueOf(Util.sanitizeBigInt(endID.toBigInteger().add(BigInteger.ONE))));
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
	
	/**
	 * resets all playerfields to UNKNOWN
	 */
	public void resetPlayer(){
		for (Field field : playerFields) {
			field.setState(FieldState.UNKNOWN);
		}
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
		String retStr = ""; 
		if (GameState.getInstance().getSelf().equals(this)){
			retStr += "Self  "; 
		}else{
			retStr += "Other "; 
		}
		retStr += "[" +startID.shortIDAsString() + " - " + endID.shortIDAsString() + "] Received shots: " + getNumberOfReceivedUniqueShots() + " Ships left: " + getNumberOfShipsLeft() + "\n[";
		for (int i = 0; i < playerFields.length; i++) {
			retStr += playerFields[i].shortRepresentation();
		}
		retStr += "]\n";
		return retStr;
	}

}
