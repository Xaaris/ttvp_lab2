import java.math.BigInteger;

import de.uniba.wiai.lspi.chord.data.ID;

public class Field {
	
	private FieldState state = FieldState.UNKNOWN;
	private final ID startID;
	private final ID endID; 
	
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
	
	

}
