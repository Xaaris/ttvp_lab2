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
		return new ID(startID.toBigInteger().add((startID.distanceTo(endID).divide(new BigInteger("2")))).toByteArray());
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
