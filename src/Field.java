import java.math.BigInteger;

import de.uniba.wiai.lspi.chord.data.ID;

public class Field {
	
	private FieldState state = FieldState.UNKNOWN;
	private ID startID;
	private ID endID; 
	
	public Field(ID startID, ID endID){
		this.startID = startID;
		this.endID = endID;
	}
	
	public Field(ID startID, ID endID, FieldState state){
		this.startID = startID;
		this.endID = endID;
		this.state = state;
	}
	
	public ID toID(){
		byte[] returnBytes = startID.toBigInteger().add(startID.distanceTo(endID).divide(new BigInteger("2"))).mod(Constants.MAXVALUE).toByteArray();
		
		byte[] shortenedArray = new byte[20];
		for (int i = 1; i <= shortenedArray.length; i++) {
			shortenedArray[shortenedArray.length - i] = returnBytes[ returnBytes.length - i];
		}
		return new ID(shortenedArray);
	}
	
	public boolean isIDInField(ID id) {
		if (id.distanceTo(endID).compareTo(startID.distanceTo(endID)) > 0) {
			return false;
		}
		return true;
	}

	public FieldState getState() {
		return state;
	}

	public void setState(FieldState state) {
		this.state = state;
	}

	public ID getStartID() {
		return startID;
	}

	public ID getEndID() {
		return endID;
	}
	
	public void setStartID(ID newStartID){
		this.startID = newStartID;
	}
	
	public void setEndID(ID newEndID){
		this.endID = newEndID;
	}
	
	
	

}
